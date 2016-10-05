/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.model.Department;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.StudentService;
import gr.roropoulos.egrades.service.UniversityService;
import gr.roropoulos.egrades.task.StudentTasks;
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

    private final Student student = new Student();
    private final StudentService studentService = new StudentService();
    private final UniversityService universityService = new UniversityService();
    private final PreferenceService preferenceService = new PreferenceService();
    private final List<University> uniList = universityService.getUniversitiesList();
    private Preference userPref;

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        initializeHandlers();
        uniList.sort((uni1, uni2) -> uni1.getUniversityName().compareTo(uni2.getUniversityName()));
        uniChoiceBox.getItems().setAll(uniList);
        uniChoiceBox.getSelectionModel().select(0);
        loadStudentData();
        userPref = preferenceService.getUserPreferences();
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

        StudentTasks studentTasks = new StudentTasks(student, userPref.getPrefAdvancedTimeout());
        Task<Map<String, String>> getCookieTask = studentTasks.getCookiesTask;

        getCookieTask.setOnSucceeded(e -> {
            if (getCookieTask.getValue() != null) {
                progressIndicator.setProgress(0.2);
                progressLabel.setText("Γίνεται συγχρονισμός των στοιχείων...");
                MainController.getInstance().clearCourseData();
                syncStudent(getCookieTask.getValue());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Αποτυχία ταυτοποίησης");
                alert.setHeaderText("Λάθος όνομα χρήστη ή κωδικός");
                alert.setContentText("Παρακαλώ ελέγξτε τα στοιχεία και δοκιμάστε ξανά.");
                alert.showAndWait();
                showLoadingScreen(false);
            }
        });

        getCookieTask.setOnFailed(e -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Αποτυχία σύνδεσης");
            alert.setHeaderText("Η σύνδεση με τη γραμματεία απέτυχε.");
            alert.setContentText("Υπήρξε πρόβλημα κατά τη σύνδεση με την υπηρεσία της γραμματείας σας.\r\n" +
                    "Δοκιμάστε αργότερα εάν η υπηρεσία είναι εκτός λειτουργίας αυτή τη στιγμή.");
            alert.showAndWait();
            showLoadingScreen(false);
        });

        Thread t = new Thread(getCookieTask);
        t.setDaemon(true);
        t.start();
    }

    private void syncStudent(Map<String, String> cookieJar) {
        studentService.setStudent(student);
        StudentTasks studentTasks = new StudentTasks(student, cookieJar, userPref.getPrefAdvancedTimeout());
        Task parseInfoTask = studentTasks.parseAndSerializeInfoTask;
        Task parseGradesTask = studentTasks.parseAndSerializeCoursesTask;
        Task parseStatsTask = studentTasks.parseAndSerializeStatsTask;
        Task parseRegTask = studentTasks.parseAndSerializeRegisterTask;

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

        if (userPref.getPrefSyncEnabled()) {
            SyncScheduler.getInstance().stopScheduler();
            SyncScheduler.getInstance().startSyncScheduler(userPref.getPrefSyncTime());
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
        if (studentService.checkIfStudentExists()) {
            Student student = studentService.getStudent();
            University selectedUni = uniList.stream()
                    .filter(item -> item.getUniversityName().equals(student.getStudentUniversity().getUniversityName()))
                    .findFirst().get();
            uniChoiceBox.getSelectionModel().select(selectedUni);
            // Pre-select saved department
            if (student.getStudentUniversityDepartment() != null) {
                List<Department> departmentList = selectedUni.getUniversityDepartment();
                Department selectedDepartment = departmentList.stream()
                        .filter(item -> item.getDepartmentName().equals(student.getStudentUniversityDepartment().getDepartmentName()))
                        .findFirst().get();
                departmentChoiceBox.getSelectionModel().select(selectedDepartment);
            }
            usernameField.setText(student.getStudentUsername());
            passwordField.setText(student.getStudentPassword());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
