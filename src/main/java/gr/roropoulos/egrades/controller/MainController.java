/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.domain.Preference;
import gr.roropoulos.egrades.eGrades;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.Impl.ExceptionServiceImpl;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @FXML
    private CheckMenuItem settingAutoSyncCheckMenuItem, settingNotificationsCheckMenuItem;

    @FXML
    private Circle statusCircle;

    @FXML
    private Label statusLabel, completedCoursesLabel, averageScoreLabel, creditsSumLabel, hoursSumLabel, ectsSumLabel;


    private PreferenceService preferenceService = new PreferenceServiceImpl();
    private ExceptionService exceptionService = new ExceptionServiceImpl();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        Platform.setImplicitExit(false);
        Preference pref = preferenceService.getPreferences();
        setUISettings(pref);
    }

    protected void setUISettings(Preference pref) {
        if(pref.getSettingAutoSync()) settingAutoSyncCheckMenuItem.setSelected(true);
        if(pref.getSettingNotifications()) settingAutoSyncCheckMenuItem.setSelected(true);
    }

    @FXML
    protected void syncMenuItemAction(ActionEvent event) {

    }

    @FXML
    protected void changeStudentMenuAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AuthView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion() + " - Αλλαγή χρήστη" );
            stage.setScene(new Scene(root));
            stage.setWidth(450);
            stage.setHeight(220);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            exceptionService.showException(e, "Δεν βρέθηκε το FXML AuthView.");
        }
    }

    @FXML
    protected void preferencesMenuAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PrefView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion() + " - Προτιμήσεις" );
            stage.setScene(new Scene(root));
            stage.setWidth(300);
            stage.setHeight(260);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            exceptionService.showException(e, "Δεν βρέθηκε το FXML PrefView.");
        }
    }

    @FXML
    protected void exitMenuAction(ActionEvent event) {

        Platform.exit();
    }

    @FXML
    protected void settingAutoSyncCheckMenuItemHandle(ActionEvent event) {
        Preference pref = preferenceService.getPreferences();
        if(settingAutoSyncCheckMenuItem.isSelected()) {
            pref.setSettingAutoSync(true);
        }
        else {
            pref.setSettingAutoSync(false);
        }
        preferenceService.setPreferences(pref);
    }

    @FXML
    protected void settingNotificationsCheckMenuItemHandle(ActionEvent event) {
        Preference pref = preferenceService.getPreferences();
        if(settingNotificationsCheckMenuItem.isSelected()) {
            pref.setSettingNotifications(true);
        }
        else {
            pref.setSettingNotifications(false);
        }
        preferenceService.setPreferences(pref);
    }

}
