/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.model.University;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    private Student student = new Student();
    private UniversityService universityService = new UniversityService();
    private SerializeService serializeService = new SerializeServiceImpl();
    private StudentParser studentParser = new CardisoftStudentParserImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();

    private List<University> uniList = universityService.getUniversitiesList();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        initializeHandlers();
        uniList.sort((uni1, uni2) -> uni1.getUniversityName().compareTo(uni2.getUniversityName()));
        uniChoiceBox.getItems().setAll(uniList);
        uniChoiceBox.getSelectionModel().select(0);

        loadStudentData();
    }

    @FXML
    protected void handleOkButtonAction() {
        MainController.getInstance().clearCourseData();
        student.setStudentUsername(usernameField.getText());
        student.setStudentPassword(passwordField.getText());

        if (serializeService.studentCheckAuthentication(student)) {
            serializeService.serializeStudent(student);
            serializeService.serializeInfo(studentParser.parseStudentInfo(student));
            serializeService.serializeCourses(studentParser.parseStudentGrades());
            serializeService.serializeStats(studentParser.parseStudentStats());
            serializeService.serializeLastRegister(studentParser.parseStudentRegistration());

            MainController.getInstance().updateAllViewComponents();

            if (preferenceService.getPreferences().getPrefSyncEnabled())
                SyncScheduler.getInstance().startSyncScheduler(preferenceService.getPreferences().getPrefSyncTime());

            dialogStage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Αποτυχία ταυτοποίησης");
            alert.setHeaderText("Λάθος όνομα χρήστη ή κωδικός");
            alert.setContentText("Παρακαλώ ελέγξτε τα στοιχεία και δοκιμάστε ξανά.");

            alert.showAndWait();
            serializeService.deleteSerializedFile();
        }

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
