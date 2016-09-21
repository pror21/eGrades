/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.model;

public class Preference {
    // Auto sync
    private Boolean prefSyncEnabled;
    private Integer prefSyncTime;

    // Notifications
    private Boolean prefNotificationPopupEnabled;
    private String prefNotificationPopupAnimation;
    private Boolean prefNotificationSoundEnabled;
    private String prefNotificationSound;

    // Mailer
    private Boolean prefMailerEnabled;
    private String prefMailerHostname;
    private Integer prefMailerPort;
    private String prefMailerUsername;
    private String prefMailerPassword;
    private Boolean prefMailerSSL;
    private String prefMailerFrom;
    private String prefMailerTo;

    // Misc
    private Boolean prefShowCloseAlert;

    // Advanced
    private Integer prefAdvancedTimeout;
    private Boolean prefAdvancedShowErrors;
    private Boolean prefAdvancedLogErrors;

    public Boolean getPrefSyncEnabled() {
        return prefSyncEnabled;
    }

    public void setPrefSyncEnabled(Boolean prefSyncEnabled) {
        this.prefSyncEnabled = prefSyncEnabled;
    }

    public Integer getPrefSyncTime() {
        return prefSyncTime;
    }

    public void setPrefSyncTime(Integer prefSyncTime) {
        this.prefSyncTime = prefSyncTime;
    }

    public Boolean getPrefNotificationPopupEnabled() {
        return prefNotificationPopupEnabled;
    }

    public void setPrefNotificationPopupEnabled(Boolean prefNotificationPopupEnabled) {
        this.prefNotificationPopupEnabled = prefNotificationPopupEnabled;
    }

    public String getPrefNotificationPopupAnimation() {
        return prefNotificationPopupAnimation;
    }

    public void setPrefNotificationPopupAnimation(String prefNotificationPopupAnimation) {
        this.prefNotificationPopupAnimation = prefNotificationPopupAnimation;
    }

    public Boolean getPrefNotificationSoundEnabled() {
        return prefNotificationSoundEnabled;
    }

    public void setPrefNotificationSoundEnabled(Boolean prefNotificationSoundEnabled) {
        this.prefNotificationSoundEnabled = prefNotificationSoundEnabled;
    }

    public String getPrefNotificationSound() {
        return prefNotificationSound;
    }

    public void setPrefNotificationSound(String prefNotificationSound) {
        this.prefNotificationSound = prefNotificationSound;
    }

    public Boolean getPrefMailerEnabled() {
        return prefMailerEnabled;
    }

    public void setPrefMailerEnabled(Boolean prefMailerEnabled) {
        this.prefMailerEnabled = prefMailerEnabled;
    }

    public String getPrefMailerHostname() {
        return prefMailerHostname;
    }

    public void setPrefMailerHostname(String prefMailerHostname) {
        this.prefMailerHostname = prefMailerHostname;
    }

    public Integer getPrefMailerPort() {
        return prefMailerPort;
    }

    public void setPrefMailerPort(Integer prefMailerPort) {
        this.prefMailerPort = prefMailerPort;
    }

    public String getPrefMailerUsername() {
        return prefMailerUsername;
    }

    public void setPrefMailerUsername(String prefMailerUsername) {
        this.prefMailerUsername = prefMailerUsername;
    }

    public String getPrefMailerPassword() {
        return prefMailerPassword;
    }

    public void setPrefMailerPassword(String prefMailerPassword) {
        this.prefMailerPassword = prefMailerPassword;
    }

    public Boolean getPrefMailerSSL() {
        return prefMailerSSL;
    }

    public void setPrefMailerSSL(Boolean prefMailerSSL) {
        this.prefMailerSSL = prefMailerSSL;
    }

    public String getPrefMailerFrom() {
        return prefMailerFrom;
    }

    public void setPrefMailerFrom(String prefMailerFrom) {
        this.prefMailerFrom = prefMailerFrom;
    }

    public String getPrefMailerTo() {
        return prefMailerTo;
    }

    public void setPrefMailerTo(String prefMailerTo) {
        this.prefMailerTo = prefMailerTo;
    }

    public Integer getPrefAdvancedTimeout() {
        return prefAdvancedTimeout;
    }

    public void setPrefAdvancedTimeout(Integer prefAdvancedTimeout) {
        this.prefAdvancedTimeout = prefAdvancedTimeout;
    }

    public Boolean getPrefAdvancedShowErrors() {
        return prefAdvancedShowErrors;
    }

    public void setPrefAdvancedShowErrors(Boolean prefAdvancedShowErrors) {
        this.prefAdvancedShowErrors = prefAdvancedShowErrors;
    }

    public Boolean getPrefAdvancedLogErrors() {
        return prefAdvancedLogErrors;
    }

    public void setPrefAdvancedLogErrors(Boolean prefAdvancedLogErrors) {
        this.prefAdvancedLogErrors = prefAdvancedLogErrors;
    }

    public Boolean getPrefShowCloseAlert() {
        return prefShowCloseAlert;
    }

    public void setPrefShowCloseAlert(Boolean prefShowCloseAlert) {
        this.prefShowCloseAlert = prefShowCloseAlert;
    }
}
