/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.service.PreferenceService;

import java.util.prefs.Preferences;

public class PreferenceServiceImpl implements PreferenceService {

    private Preferences prefs;

    public Preference getPreferences() {
        Preference pref = new Preference();
        prefs = Preferences.userRoot().node("egrades/preferences");

        pref.setPrefSyncEnabled(prefs.getBoolean("prefSyncEnabled", true));
        pref.setPrefSyncTime(prefs.getInt("prefSyncTime", 30));

        pref.setPrefNotificationPopupEnabled(prefs.getBoolean("prefNotificationPopupEnabled", true));
        pref.setPrefNotificationPopupAnimation(prefs.get("prefNotificationPopupAnimation", "popup"));
        pref.setPrefNotificationSoundEnabled(prefs.getBoolean("prefNotificationSoundEnabled", false));
        pref.setPrefNotificationSound(prefs.get("prefNotificationSound", "arpeggio"));

        pref.setPrefMailerEnabled(prefs.getBoolean("prefMailerEnabled", false));
        pref.setPrefMailerHostname(prefs.get("prefMailerHostname", "smtp.gmail.com"));
        pref.setPrefMailerPort(prefs.getInt("prefMailerPort", 465));
        pref.setPrefMailerUsername(prefs.get("prefMailerUsername", ""));
        pref.setPrefMailerPassword(prefs.get("prefMailerPassword", ""));
        pref.setPrefMailerSSL(prefs.getBoolean("prefMailerSsl", true));
        pref.setPrefMailerFrom(prefs.get("prefMailerFrom", ""));
        pref.setPrefMailerTo(prefs.get("prefMailerTo", ""));

        pref.setPrefStartOnBoot(prefs.getBoolean("prefStartOnBoot", true));
        pref.setPrefKeepRunning(prefs.getBoolean("prefKeepRunning", true));
        pref.setPrefShowCloseAlert(prefs.getBoolean("prefShowCloseAlert", false));

        pref.setPrefAdvancedTimeout(prefs.getInt("prefTimeout", 20000));
        pref.setPrefAdvancedShowErrors(prefs.getBoolean("prefShowBugAlerts", false));
        pref.setPrefAdvancedLogErrors(prefs.getBoolean("prefLogDebug", false));

        return pref;
    }

    public void setPreferences(Preference pref) {
        prefs = Preferences.userRoot().node("egrades/preferences");

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

        prefs.putBoolean("prefStartOnBoot", pref.getPrefStartOnBoot());
        prefs.putBoolean("prefKeepRunning", pref.getPrefKeepRunning());
        prefs.putBoolean("prefShowCloseAlert", pref.getPrefShowCloseAlert());

        prefs.putInt("prefTimeout", pref.getPrefAdvancedTimeout());
        prefs.putBoolean("prefShowBugAlerts", pref.getPrefAdvancedShowErrors());
        prefs.putBoolean("prefLogDebug", pref.getPrefAdvancedLogErrors());
    }

}
