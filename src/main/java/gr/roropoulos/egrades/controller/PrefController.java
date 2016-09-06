/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PrefController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PrefController.class);
    private Stage dialogStage;

    @FXML
    private CheckBox popupCheckBox, soundCheckBox, statusBarCheckBox, startUpCheckBox, checkUpdateCheckBox, showAlertsCheckBox, logDebugCheckBox;
    @FXML
    private TextField syncTimeTextField, timeoutTextField;
    @FXML
    private Button resetButton, saveButton, cancelButton;
    @FXML
    private Accordion preferencesAccordion;
    @FXML
    private TitledPane generalPrefTitledPane, advancedPrefTitledPane;

    private Preference pref = new Preference();
    private PreferenceService preferenceService = new PreferenceServiceImpl();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        pref = preferenceService.getPreferences();

        TextFormatter<Integer> syncTimeFormatter = new TextFormatter<Integer>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        });

        TextFormatter<Integer> timeoutFormatter = new TextFormatter<Integer>(change -> {
            change.setText(change.getText().replaceAll("[^0-9]", ""));
            return change;
        });

        syncTimeTextField.setTextFormatter(syncTimeFormatter);
        timeoutTextField.setTextFormatter(timeoutFormatter);

        preferencesAccordion.setExpandedPane(generalPrefTitledPane);
        setUIPreferences(pref);
    }

    protected void setUIPreferences(Preference pref) {
        syncTimeTextField.setText(pref.getPrefSyncTime().toString());
        timeoutTextField.setText(pref.getPrefTimeout().toString());
        if (pref.getPrefPopupNotification()) popupCheckBox.setSelected(true);
        if (pref.getPrefSoundNotification()) soundCheckBox.setSelected(true);
        if (pref.getPrefShowStatusBar()) statusBarCheckBox.setSelected(true);
        if (pref.getPrefStartOnBoot()) startUpCheckBox.setSelected(true);
        if (pref.getPrefCheckForUpdates()) checkUpdateCheckBox.setSelected(true);
        if (pref.getPrefShowBugAlerts()) showAlertsCheckBox.setSelected(true);
        if (pref.getPrefLogDebug()) logDebugCheckBox.setSelected(true);
    }

    @FXML
    private void savePrefButtonAction(ActionEvent event) {
        pref.setPrefSyncTime(Integer.parseInt(syncTimeTextField.getText()));
        pref.setPrefTimeout(Integer.parseInt(timeoutTextField.getText()));
        if (popupCheckBox.isSelected()) pref.setPrefPopupNotification(true);
        else pref.setPrefPopupNotification(false);
        if (soundCheckBox.isSelected()) pref.setPrefSoundNotification(true);
        else pref.setPrefSoundNotification(false);
        if (statusBarCheckBox.isSelected()) pref.setPrefShowStatusBar(true);
        else pref.setPrefShowStatusBar(false);
        if (startUpCheckBox.isSelected()) pref.setPrefStartOnBoot(true);
        else pref.setPrefStartOnBoot(false);
        if (checkUpdateCheckBox.isSelected()) pref.setPrefCheckForUpdates(true);
        else pref.setPrefCheckForUpdates(false);
        if (showAlertsCheckBox.isSelected()) pref.setPrefShowBugAlerts(true);
        else pref.setPrefShowBugAlerts(false);
        if (logDebugCheckBox.isSelected()) pref.setPrefLogDebug(true);
        else pref.setPrefLogDebug(false);
        savePreferences();
        dialogStage.close();
    }

    @FXML
    private void cancelPrefButtonAction(ActionEvent event) {
        dialogStage.close();
    }

    private void savePreferences() {
        preferenceService.setPreferences(pref);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
