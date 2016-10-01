/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.model.Department;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.repository.TaskRepository;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.Impl.SerializeServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.SerializeService;
import gr.roropoulos.egrades.service.UniversityService;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthController implements Initializable {
    private Stage dialogStage;

    @FXML
    private ChoiceBox<University> uniChoiceBox;
    @FXML
    private ChoiceBox<Department> departmentChoiceBox;
    @FXML
    private Button okButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label progressLabel;
    @FXML
    private BorderPane loadingPane;
    @FXML
    private GridPane loginPane;

    private Student student = new Student();
    private UniversityService universityService = new UniversityService();
    private SerializeService serializeService = new SerializeServiceImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();

    private List<University> uniList = universityService.getUniversitiesList();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        initializeHandlers();
        uniList.sort((uni1, uni2) -> uni1.getUniversityName().compareTo(uni2.getUniversityName()));
        uniChoiceBox.getItems().setAll(uniList);
        uniChoiceBox.getSelectionModel().select(0);

        loadStudentData();
    }

    private void showLoadingScreen(Boolean show) {
        if (show) {
            loginPane.setVisible(false);
            loadingPane.setVisible(true);
        } else {
            loadingPane.setVisible(false);
            loginPane.setVisible(true);
        }
    }

    @FXML
    protected void handleOkButtonAction() {
        student.setStudentUsername(usernameField.getText());
        student.setStudentPassword(passwordField.getText());
        SyncScheduler.getInstance().stopScheduler();
        MainController.getInstance().clearCourseData();
        showLoadingScreen(true);
        progressLabel.setText("Γίνεται ταυτοποίηση...");

        TaskRepository taskRepository = new TaskRepository(student);
        Task<Map<String, String>> getCookieTask = taskRepository.getCookiesTask;

        getCookieTask.setOnSucceeded(e -> {
            if (getCookieTask.getValue() != null) {
                progressIndicator.setProgress(0.2);
                progressLabel.setText("Γίνεται συγχρονισμός των στοιχείων...");
                MainController.getInstance().clearCourseData();
                serializeService.serializeStudent(student);
                syncStudent(getCookieTask.getValue());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Αποτυχία ταυτοποίησης");
                alert.setHeaderText("Λάθος όνομα χρήστη ή κωδικός");
                alert.setContentText("Παρακαλώ ελέγξτε τα στοιχεία και δοκιμάστε ξανά.");
                serializeService.deleteSerializedFile();
                alert.showAndWait();
                showLoadingScreen(false);
            }
        });

        Thread t = new Thread(getCookieTask);
        t.setDaemon(true);
        t.start();
    }

    private void syncStudent(Map<String, String> cookieJar) {
        TaskRepository taskRepository = new TaskRepository(student, cookieJar);
        Task parseInfoTask = taskRepository.parseAndSerializeInfoTask;
        Task parseGradesTask = taskRepository.parseAndSerializeCoursesTask;
        Task parseStatsTask = taskRepository.parseAndSerializeStatsTask;
        Task parseRegTask = taskRepository.parseAndSerializeRegisterTask;

        parseInfoTask.setOnSucceeded(e -> {
            progressIndicator.setProgress(0.4);
            progressLabel.setText("Γίνεται συγχρονισμός των μαθημάτων...");

        });

        parseGradesTask.setOnSucceeded(e -> {
            progressIndicator.setProgress(0.6);
            progressLabel.setText("Γίνεται συγχρονισμός των συνόλων...");

        });

        parseStatsTask.setOnSucceeded(e -> {
            progressIndicator.setProgress(0.8);
            progressLabel.setText("Γίνεται συγχρονισμός της δήλωσης...");

        });

        parseRegTask.setOnSucceeded(e -> {
            progressIndicator.setProgress(1);
            progressLabel.setText("Επιτυχής συγχρονισμός!");
            updateMainComponents();
        });

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(parseInfoTask);
        es.submit(parseGradesTask);
        es.submit(parseStatsTask);
        es.submit(parseRegTask);
        es.shutdown();

    }

    private void updateMainComponents() {
        MainController.getInstance().updateAllViewComponents();

        if (preferenceService.getPreferences().getPrefSyncEnabled()) {
            SyncScheduler.getInstance().stopScheduler();
            SyncScheduler.getInstance().startSyncScheduler(preferenceService.getPreferences().getPrefSyncTime());
        }
        dialogStage.close();
    }

    @FXML
    protected void handleCancelButtonAction() {
        dialogStage.close();
    }

    private void initializeHandlers() {
        // Set Student university selection
        uniChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    student.setStudentUniversity(newValue);
                    if (newValue.getUniversityDepartment() != null && !newValue.getUniversityDepartment().isEmpty()) {
                        departmentChoiceBox.getItems().setAll(newValue.getUniversityDepartment());
                        departmentChoiceBox.getSelectionModel().select(0);
                        departmentChoiceBox.setDisable(false);
                    } else {
                        departmentChoiceBox.getItems().clear();
                        departmentChoiceBox.setDisable(true);
                    }
                }

        );

        // Set student department selection
        departmentChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null)
                        student.setStudentUniversityDepartment(newValue);

                    else
                        student.setStudentUniversityDepartment(null);
                }
        );

        // Require Username and Password for enabling OK Button
        BooleanBinding okButtonBooleanBind = usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty());
        okButton.disableProperty().bind(okButtonBooleanBind);
    }

    private void loadStudentData() {
        // Pre-select saved university
        if (serializeService.checkIfSerializedFileExist()) {
            University selectedUni = uniList.stream()
                    .filter(item -> item.getUniversityName().equals(serializeService.deserializeStudent().getStudentUniversity().getUniversityName()))
                    .findFirst().get();
            uniChoiceBox.getSelectionModel().select(selectedUni);
            // Pre-select saved department
            if (serializeService.deserializeStudent().getStudentUniversityDepartment() != null) {
                List<Department> departmentList = selectedUni.getUniversityDepartment();
                Department selectedDepartment = departmentList.stream()
                        .filter(item -> item.getDepartmentName().equals(serializeService.deserializeStudent().getStudentUniversityDepartment().getDepartmentName()))
                        .findFirst().get();
                departmentChoiceBox.getSelectionModel().select(selectedDepartment);
            }
            usernameField.setText(serializeService.deserializeStudent().getStudentUsername());
            passwordField.setText(serializeService.deserializeStudent().getStudentPassword());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
