/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import com.github.plushaze.traynotification.animations.Animations;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.notifier.GradeNotifier;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrefController implements Initializable {
    private Stage dialogStage;

    @FXML
    private CheckBox autoSyncCheckBox, popupNotificationCheckBox, soundNotificationCheckBox,
            enableMailerCheckBox, sslCheckBox, showErrorsCheckBox, logErrorsCheckBox, showCloseAlertCheckBox;
    @FXML
    private TextField syncRateTextField, hostnameTextField, portTextField, usernameTextField, passwordTextField,
            fromTextField, toTextField, timeoutTextField;
    @FXML
    private ChoiceBox<String> popupEffectChoiceBox, soundChoiceBox;


    private Preference pref = new Preference();
    private PreferenceService preferenceService = new PreferenceServiceImpl();
    private GradeNotifier gradeNotifier = new GradeNotifier();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        pref = preferenceService.getPreferences();

        TextFormatter<Integer> syncTimeFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        });

        TextFormatter<Integer> timeoutFormatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        });

        syncRateTextField.setTextFormatter(syncTimeFormatter);
        timeoutTextField.setTextFormatter(timeoutFormatter);

        setUIPreferences(pref);
    }

    private void setUIPreferences(Preference pref) {
        if (pref.getPrefSyncEnabled()) autoSyncCheckBox.setSelected(true);
        if (pref.getPrefNotificationPopupEnabled()) popupNotificationCheckBox.setSelected(true);
        if (pref.getPrefNotificationSoundEnabled()) soundNotificationCheckBox.setSelected(true);
        if (pref.getPrefShowCloseAlert()) showCloseAlertCheckBox.setSelected(true);
        if (pref.getPrefMailerEnabled()) enableMailerCheckBox.setSelected(true);
        if (pref.getPrefMailerSSL()) sslCheckBox.setSelected(true);
        if (pref.getPrefAdvancedShowErrors()) showErrorsCheckBox.setSelected(true);
        if (pref.getPrefAdvancedLogErrors()) logErrorsCheckBox.setSelected(true);

        syncRateTextField.setText(pref.getPrefSyncTime().toString());
        timeoutTextField.setText(pref.getPrefAdvancedTimeout().toString());
        hostnameTextField.setText(pref.getPrefMailerHostname());
        portTextField.setText(pref.getPrefMailerPort().toString());
        usernameTextField.setText(pref.getPrefMailerUsername());
        passwordTextField.setText(pref.getPrefMailerPassword());
        fromTextField.setText(pref.getPrefMailerFrom());
        toTextField.setText(pref.getPrefMailerTo());

        popupEffectChoiceBox.setItems(FXCollections.observableArrayList("popup", "slide", "fade"));
        popupEffectChoiceBox.valueProperty().addListener((ov, t, t1) -> {
            if (t != null) {
                if (Objects.equals(t1, "popup"))
                    gradeNotifier.showSampleNotification(Animations.POPUP);
                else if (Objects.equals(t1, "slide"))
                    gradeNotifier.showSampleNotification(Animations.SLIDE);
                else if (Objects.equals(t1, "fade"))
                    gradeNotifier.showSampleNotification(Animations.FADE);
            }
        });

        popupEffectChoiceBox.getSelectionModel().select(pref.getPrefNotificationPopupAnimation());
        soundChoiceBox.setItems(FXCollections.observableArrayList("arpeggio", "attention", "ding", "pluck"));
        soundChoiceBox.valueProperty().addListener((ov, t, t1) -> {
            if (t != null) {
                if (Objects.equals(t1, "arpeggio"))
                    gradeNotifier.playSoundNotification("arpeggio");
                else if (Objects.equals(t1, "attention"))
                    gradeNotifier.playSoundNotification("attention");
                else if (Objects.equals(t1, "ding"))
                    gradeNotifier.playSoundNotification("ding");
                else if (Objects.equals(t1, "pluck"))
                    gradeNotifier.playSoundNotification("pluck");
            }
        });
        soundChoiceBox.getSelectionModel().select(pref.getPrefNotificationSound());
    }

    @FXML
    private void savePrefButtonAction() {
        if (autoSyncCheckBox.isSelected()) pref.setPrefSyncEnabled(true);
        else pref.setPrefSyncEnabled(false);
        if (popupNotificationCheckBox.isSelected()) pref.setPrefNotificationPopupEnabled(true);
        else pref.setPrefNotificationPopupEnabled(false);
        if (soundNotificationCheckBox.isSelected()) pref.setPrefNotificationSoundEnabled(true);
        else pref.setPrefNotificationSoundEnabled(false);
        if (showCloseAlertCheckBox.isSelected()) pref.setPrefShowCloseAlert(true);
        else pref.setPrefShowCloseAlert(false);
        if (enableMailerCheckBox.isSelected()) pref.setPrefMailerEnabled(true);
        else pref.setPrefMailerEnabled(false);
        if (sslCheckBox.isSelected()) pref.setPrefMailerSSL(true);
        else pref.setPrefMailerSSL(false);
        if (showErrorsCheckBox.isSelected()) pref.setPrefAdvancedShowErrors(true);
        else pref.setPrefAdvancedShowErrors(false);
        if (logErrorsCheckBox.isSelected()) pref.setPrefAdvancedLogErrors(true);
        else pref.setPrefAdvancedLogErrors(false);

        pref.setPrefSyncTime(Integer.parseInt(syncRateTextField.getText()));
        pref.setPrefAdvancedTimeout(Integer.parseInt(timeoutTextField.getText()));
        pref.setPrefMailerHostname(hostnameTextField.getText());
        pref.setPrefMailerPort(Integer.parseInt(portTextField.getText()));
        pref.setPrefMailerUsername(usernameTextField.getText());
        pref.setPrefMailerPassword(passwordTextField.getText());
        pref.setPrefMailerFrom(fromTextField.getText());
        pref.setPrefMailerTo(toTextField.getText());

        pref.setPrefNotificationPopupAnimation(popupEffectChoiceBox.getValue());
        pref.setPrefNotificationSound(soundChoiceBox.getValue());

        savePreferences();
        dialogStage.close();
    }

    @FXML
    private void cancelPrefButtonAction() {
        dialogStage.close();
    }

    private void savePreferences() {

        SyncScheduler.getInstance().stopScheduler();
        if (pref.getPrefSyncEnabled())
            SyncScheduler.getInstance().startSyncScheduler(pref.getPrefSyncTime());

        preferenceService.setPreferences(pref);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
