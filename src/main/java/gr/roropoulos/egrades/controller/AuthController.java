/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.model.University;
import gr.roropoulos.egrades.parser.DocumentParser;
import gr.roropoulos.egrades.parser.Impl.CardisoftDocumentParserImpl;
import gr.roropoulos.egrades.parser.Impl.CardisoftStudentParserImpl;
import gr.roropoulos.egrades.parser.StudentParser;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.Impl.SerializeServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.SerializeService;
import gr.roropoulos.egrades.service.UniversityService;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.jsoup.Connection;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthController implements Initializable {
    private Stage dialogStage;

    @FXML
    private ChoiceBox uniChoiceBox;
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
    private StudentParser studentParser = new CardisoftStudentParserImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();
    private DocumentParser documentParser = new CardisoftDocumentParserImpl();

    private List<University> uniList = universityService.getUniversitiesList();
    private Connection.Response res;
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
        showLoadingScreen(true);
        progressLabel.setText("Γίνεται ταυτοποίηση...");

        Task<Map<String, String>> getCookieTask = new Task<Map<String, String>>() {
            @Override
            public Map<String, String> call() {
                res = documentParser.getConnection(student.getStudentUniversity());
                return documentParser.getCookies(res, student.getStudentUniversity(), usernameField.getText(), passwordField.getText());
            }
        };

        getCookieTask.setOnSucceeded(e -> {
            if (getCookieTask.getValue() != null) {
                progressIndicator.setProgress(0.2);
                progressLabel.setText("Γίνεται συγχρονισμός των στοιχείων...");
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

    private void syncStudent(Map<String, String> cookierJar) {
        MainController.getInstance().clearCourseData();
        serializeService.serializeStudent(student);

        Task parseInfoTask = new Task<Void>() {
            @Override
            public Void call() {
                HashMap<String, String> infoMap = studentParser.parseStudentInfo(student.getStudentUniversity(), cookierJar);
                serializeService.serializeInfo(infoMap);
                return null;
            }
        };

        Task parseGradesTask = new Task<Void>() {
            @Override
            public Void call() {
                List<Course> courseList = studentParser.parseStudentGrades(student.getStudentUniversity(), cookierJar);
                serializeService.serializeCourses(courseList);
                return null;
            }
        };

        Task parseStatsTask = new Task<Void>() {
            @Override
            public Void call() {
                HashMap<String, String> statsMap = studentParser.parseStudentStats(student.getStudentUniversity(), cookierJar);
                serializeService.serializeStats(statsMap);
                return null;
            }
        };

        Task parseRegTask = new Task<Void>() {
            @Override
            public Void call() {
                HashMap<String, String> regMap = studentParser.parseStudentRegistration(student.getStudentUniversity(), cookierJar);
                List<Course> regList = serializeService.fetchRegisterCourseList(regMap);
                serializeService.serializeRegister(regList);
                return null;
            }
        };

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
        uniChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<University>() {
            @Override
            public void changed(ObservableValue<? extends University> observable, University oldValue, University newValue) {
                student.setStudentUniversity(newValue);
            }
        });

        // Require Username and Password for enabling OK Button
        BooleanBinding okButtonBooleanBind = usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty());
        okButton.disableProperty().bind(okButtonBooleanBind);
    }

    private void loadStudentData() {
        if (serializeService.checkIfSerializedFileExist()) {
            University selectedUni = uniList.stream()
                    .filter(item -> item.getUniversityName().equals(serializeService.deserializeStudent().getStudentUniversity().getUniversityName()))
                    .findFirst().get();
            uniChoiceBox.getSelectionModel().select(selectedUni);
            usernameField.setText(serializeService.deserializeStudent().getStudentUsername());
            passwordField.setText(serializeService.deserializeStudent().getStudentPassword());
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
