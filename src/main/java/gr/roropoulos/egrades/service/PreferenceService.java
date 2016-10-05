/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service;

import gr.roropoulos.egrades.dao.Impl.PreferenceDAOImpl;
import gr.roropoulos.egrades.dao.PreferenceDAO;
import gr.roropoulos.egrades.model.Preference;

import java.util.prefs.Preferences;

public class PreferenceService {

    private final PreferenceDAO preferenceDAO = new PreferenceDAOImpl();

    public Preference getUserPreferences() {
        Preferences prefs = preferenceDAO.getPreferences();
        Preference userPreferences = new Preference();

        userPreferences.setPrefSyncEnabled(prefs.getBoolean("prefSyncEnabled", true));
        userPreferences.setPrefSyncTime(prefs.getInt("prefSyncTime", 30));

        userPreferences.setPrefNotificationPopupEnabled(prefs.getBoolean("prefNotificationPopupEnabled", true));
        userPreferences.setPrefNotificationPopupAnimation(prefs.get("prefNotificationPopupAnimation", "popup"));
        userPreferences.setPrefNotificationSoundEnabled(prefs.getBoolean("prefNotificationSoundEnabled", true));
        userPreferences.setPrefNotificationSound(prefs.get("prefNotificationSound", "arpeggio"));

        userPreferences.setPrefMailerEnabled(prefs.getBoolean("prefMailerEnabled", false));
        userPreferences.setPrefMailerHostname(prefs.get("prefMailerHostname", "smtp.gmail.com"));
        userPreferences.setPrefMailerPort(prefs.getInt("prefMailerPort", 465));
        userPreferences.setPrefMailerUsername(prefs.get("prefMailerUsername", ""));
        userPreferences.setPrefMailerPassword(prefs.get("prefMailerPassword", ""));
        userPreferences.setPrefMailerSSL(prefs.getBoolean("prefMailerSsl", true));
        userPreferences.setPrefMailerFrom(prefs.get("prefMailerFrom", ""));
        userPreferences.setPrefMailerTo(prefs.get("prefMailerTo", ""));

        userPreferences.setPrefShowCloseAlert(prefs.getBoolean("prefShowCloseAlert", true));

        userPreferences.setPrefAdvancedTimeout(prefs.getInt("prefTimeout", 20000));
        userPreferences.setPrefAdvancedShowErrors(prefs.getBoolean("prefShowBugAlerts", false));
        userPreferences.setPrefAdvancedLogErrors(prefs.getBoolean("prefLogDebug", true));

        return userPreferences;
    }

    public void saveUserPreferences(Preference userPreferences) {
        preferenceDAO.savePreferences(userPreferences);
    }
}
