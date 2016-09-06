/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

public class Preference {

    private Integer prefSyncTime;
    private Boolean prefPopupNotification;
    private Boolean prefSoundNotification;
    private Boolean prefShowStatusBar;
    private Boolean prefStartOnBoot;
    private Boolean prefCheckForUpdates;
    private Integer prefTimeout;
    private Boolean prefShowBugAlerts;
    private Boolean prefLogDebug;

    private Boolean settingAutoSync;
    private Boolean settingNotifications;

    public Integer getPrefSyncTime() {
        return prefSyncTime;
    }

    public void setPrefSyncTime(Integer prefSyncTime) {
        this.prefSyncTime = prefSyncTime;
    }

    public Boolean getPrefPopupNotification() {
        return prefPopupNotification;
    }

    public void setPrefPopupNotification(Boolean prefPopupNotification) {
        this.prefPopupNotification = prefPopupNotification;
    }

    public Boolean getPrefSoundNotification() {
        return prefSoundNotification;
    }

    public void setPrefSoundNotification(Boolean prefSoundNotification) {
        this.prefSoundNotification = prefSoundNotification;
    }

    public Boolean getPrefShowStatusBar() {
        return prefShowStatusBar;
    }

    public void setPrefShowStatusBar(Boolean prefShowStatusBar) {
        this.prefShowStatusBar = prefShowStatusBar;
    }

    public Boolean getPrefStartOnBoot() {
        return prefStartOnBoot;
    }

    public void setPrefStartOnBoot(Boolean prefStartOnBoot) {
        this.prefStartOnBoot = prefStartOnBoot;
    }

    public Boolean getPrefCheckForUpdates() {
        return prefCheckForUpdates;
    }

    public void setPrefCheckForUpdates(Boolean prefCheckForUpdates) {
        this.prefCheckForUpdates = prefCheckForUpdates;
    }

    public Integer getPrefTimeout() {
        return prefTimeout;
    }

    public void setPrefTimeout(Integer prefTimeout) {
        this.prefTimeout = prefTimeout;
    }

    public Boolean getPrefShowBugAlerts() {
        return prefShowBugAlerts;
    }

    public void setPrefShowBugAlerts(Boolean prefShowBugAlerts) {
        this.prefShowBugAlerts = prefShowBugAlerts;
    }

    public Boolean getPrefLogDebug() {
        return prefLogDebug;
    }

    public void setPrefLogDebug(Boolean prefLogDebug) {
        this.prefLogDebug = prefLogDebug;
    }

    public Boolean getSettingAutoSync() {
        return settingAutoSync;
    }

    public void setSettingAutoSync(Boolean settingAutoSync) {
        this.settingAutoSync = settingAutoSync;
    }

    public Boolean getSettingNotifications() {
        return settingNotifications;
    }

    public void setSettingNotifications(Boolean settingNotifications) {
        this.settingNotifications = settingNotifications;
    }
}
