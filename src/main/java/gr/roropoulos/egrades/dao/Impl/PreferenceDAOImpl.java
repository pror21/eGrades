/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.dao.Impl;

import gr.roropoulos.egrades.dao.PreferenceDAO;
import gr.roropoulos.egrades.model.Preference;

import java.util.prefs.Preferences;

public class PreferenceDAOImpl implements PreferenceDAO {
    public Preferences getPreferences() {
        return Preferences.userRoot().node("egrades/preferences");
    }

    public void savePreferences(Preference pref) {
        Preferences prefs = Preferences.userRoot().node("egrades/preferences");

        prefs.putBoolean("prefSyncEnabled", pref.getPrefSyncEnabled());
        prefs.putInt("prefSyncTime", pref.getPrefSyncTime());

        prefs.putBoolean("prefNotificationPopupEnabled", pref.getPrefNotificationPopupEnabled());
        prefs.put("prefNotificationPopupAnimation", pref.getPrefNotificationPopupAnimation());
        prefs.putBoolean("prefNotificationSoundEnabled", pref.getPrefNotificationSoundEnabled());
        prefs.put("prefNotificationSound", pref.getPrefNotificationSound());

        prefs.putBoolean("prefMailerEnabled", pref.getPrefMailerEnabled());
        prefs.put("prefMailerHostname", pref.getPrefMailerHostname());
        prefs.putInt("prefMailerPort", pref.getPrefMailerPort());
        prefs.put("prefMailerUsername", pref.getPrefMailerUsername());
        prefs.put("prefMailerPassword", pref.getPrefMailerPassword());
        prefs.putBoolean("prefMailerSsl", pref.getPrefMailerSSL());
        prefs.put("prefMailerFrom", pref.getPrefMailerFrom());
        prefs.put("prefMailerTo", pref.getPrefMailerTo());

        prefs.putBoolean("prefShowCloseAlert", pref.getPrefShowCloseAlert());

        prefs.putInt("prefTimeout", pref.getPrefAdvancedTimeout());
        prefs.putBoolean("prefShowBugAlerts", pref.getPrefAdvancedShowErrors());
        prefs.putBoolean("prefLogDebug", pref.getPrefAdvancedLogErrors());
    }
}
