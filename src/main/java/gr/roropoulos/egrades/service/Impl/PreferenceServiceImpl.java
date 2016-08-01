/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service.Impl;

import gr.roropoulos.egrades.domain.Preference;
import gr.roropoulos.egrades.service.PreferenceService;

import java.util.prefs.Preferences;

public class PreferenceServiceImpl implements PreferenceService {

    private Preferences prefs;

    public Preference getPreferences() {
        Preference pref = new Preference();
        prefs = Preferences.userRoot().node("egrades/preferences");
        pref.setPrefSyncTime(prefs.getInt("prefSyncTime", 30));
        pref.setPrefPopupNotification(prefs.getBoolean("prefPopupNotification", true));
        pref.setPrefSoundNotification(prefs.getBoolean("prefSoundNotification", false));
        pref.setPrefShowStatusBar(prefs.getBoolean("prefShowStatusBar", true));
        pref.setPrefStartOnBoot(prefs.getBoolean("prefStartOnBoot", true));
        pref.setPrefCheckForUpdates(prefs.getBoolean("prefCheckForUpdates", true));
        pref.setPrefTimeout(prefs.getInt("prefTimeout", 20000));
        pref.setPrefShowBugAlerts(prefs.getBoolean("prefShowBugAlerts", false));
        pref.setPrefLogDebug(prefs.getBoolean("prefLogDebug", false));
        pref.setSettingAutoSync(prefs.getBoolean("settingAutoSync", true));
        pref.setSettingNotifications(prefs.getBoolean("settingNotifications", true));
        return pref;
    }

    public void setPreferences(Preference pref) {
        prefs = Preferences.userRoot().node("egrades/preferences");
        prefs.putInt("prefSyncTime", pref.getPrefSyncTime());
        prefs.putBoolean("prefPopupNotification", pref.getPrefPopupNotification());
        prefs.putBoolean("prefSoundNotification", pref.getPrefSoundNotification());
        prefs.putBoolean("prefShowStatusBar", pref.getPrefShowStatusBar());
        prefs.putBoolean("prefStartOnBoot", pref.getPrefStartOnBoot());
        prefs.putBoolean("prefCheckForUpdates", pref.getPrefCheckForUpdates());
        prefs.putInt("prefTimeout", pref.getPrefTimeout());
        prefs.putBoolean("prefShowBugAlerts", pref.getPrefShowBugAlerts());
        prefs.putBoolean("prefLogDebug", pref.getPrefLogDebug());
        prefs.putBoolean("settingAutoSync", pref.getSettingAutoSync());
        prefs.putBoolean("settingNotifications", pref.getSettingNotifications());
    }

}
