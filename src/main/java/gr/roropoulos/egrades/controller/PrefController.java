/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import com.github.plushaze.traynotification.animations.Animations;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.NotificationService;
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

    private final PreferenceService preferenceService = new PreferenceService();
    private final NotificationService notificationService = new NotificationService();

    private Preference userPreferences = new Preference();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        userPreferences = preferenceService.getUserPreferences();

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

        setUIPreferences(userPreferences);
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
                    notificationService.showSampleNotification(Animations.POPUP);
                else if (Objects.equals(t1, "slide"))
                    notificationService.showSampleNotification(Animations.SLIDE);
                else if (Objects.equals(t1, "fade"))
                    notificationService.showSampleNotification(Animations.FADE);
            }
        });

        popupEffectChoiceBox.getSelectionModel().select(pref.getPrefNotificationPopupAnimation());
        soundChoiceBox.setItems(FXCollections.observableArrayList("arpeggio", "attention", "ding", "pluck"));
        soundChoiceBox.valueProperty().addListener((ov, t, t1) -> {
            if (t != null) {
                if (Objects.equals(t1, "arpeggio"))
                    notificationService.playSoundNotification("arpeggio");
                else if (Objects.equals(t1, "attention"))
                    notificationService.playSoundNotification("attention");
                else if (Objects.equals(t1, "ding"))
                    notificationService.playSoundNotification("ding");
                else if (Objects.equals(t1, "pluck"))
                    notificationService.playSoundNotification("pluck");
            }
        });
        soundChoiceBox.getSelectionModel().select(pref.getPrefNotificationSound());
    }

    @FXML
    private void savePrefButtonAction() {
        if (autoSyncCheckBox.isSelected()) userPreferences.setPrefSyncEnabled(true);
        else userPreferences.setPrefSyncEnabled(false);
        if (popupNotificationCheckBox.isSelected()) userPreferences.setPrefNotificationPopupEnabled(true);
        else userPreferences.setPrefNotificationPopupEnabled(false);
        if (soundNotificationCheckBox.isSelected()) userPreferences.setPrefNotificationSoundEnabled(true);
        else userPreferences.setPrefNotificationSoundEnabled(false);
        if (showCloseAlertCheckBox.isSelected()) userPreferences.setPrefShowCloseAlert(true);
        else userPreferences.setPrefShowCloseAlert(false);
        if (enableMailerCheckBox.isSelected()) userPreferences.setPrefMailerEnabled(true);
        else userPreferences.setPrefMailerEnabled(false);
        if (sslCheckBox.isSelected()) userPreferences.setPrefMailerSSL(true);
        else userPreferences.setPrefMailerSSL(false);
        if (showErrorsCheckBox.isSelected()) userPreferences.setPrefAdvancedShowErrors(true);
        else userPreferences.setPrefAdvancedShowErrors(false);
        if (logErrorsCheckBox.isSelected()) userPreferences.setPrefAdvancedLogErrors(true);
        else userPreferences.setPrefAdvancedLogErrors(false);

        userPreferences.setPrefSyncTime(Integer.parseInt(syncRateTextField.getText()));
        userPreferences.setPrefAdvancedTimeout(Integer.parseInt(timeoutTextField.getText()));
        userPreferences.setPrefMailerHostname(hostnameTextField.getText());
        userPreferences.setPrefMailerPort(Integer.parseInt(portTextField.getText()));
        userPreferences.setPrefMailerUsername(usernameTextField.getText());
        userPreferences.setPrefMailerPassword(passwordTextField.getText());
        userPreferences.setPrefMailerFrom(fromTextField.getText());
        userPreferences.setPrefMailerTo(toTextField.getText());

        userPreferences.setPrefNotificationPopupAnimation(popupEffectChoiceBox.getValue());
        userPreferences.setPrefNotificationSound(soundChoiceBox.getValue());

        savePreferences();
        dialogStage.close();
    }

    @FXML
    private void cancelPrefButtonAction() {
        dialogStage.close();
    }

    @FXML
    private void sendSampleEmailButtonAction() {
        notificationService.sendSampleMail(
                hostnameTextField.getText(),
                portTextField.getText(),
                usernameTextField.getText(),
                passwordTextField.getText(),
                sslCheckBox.isSelected(),
                fromTextField.getText(),
                toTextField.getText()
        );
    }

    private void savePreferences() {
        if (!Objects.equals(syncRateTextField.getText(), preferenceService.getUserPreferences().getPrefSyncTime().toString())) {
            SyncScheduler.getInstance().stopScheduler();
            if (userPreferences.getPrefSyncEnabled())
                SyncScheduler.getInstance().startSyncScheduler(userPreferences.getPrefSyncTime());
        }
        preferenceService.saveUserPreferences(userPreferences);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
