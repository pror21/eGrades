/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.service;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import com.github.plushaze.traynotification.notification.TrayNotification;
import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.commons.mail.*;

import java.net.URL;

public class NotifyService {

    private ExceptionService exceptionService = new ExceptionService();

    public void showNotification(String title, String message, Notifications notification, Animations animation) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(animation);
        tray.showAndWait();
    }

    public void playSoundNotification(String sound) {
        URL resource = getClass().getResource("/sounds/" + sound + ".mp3");
        Media media = new Media(resource.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void showSampleNotification(Animations animation) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle("Υπόδειγμα Ειδοποίησης");
        tray.setMessage("Αυτό είναι ένα υπόδειγμα ειδοποίησης.");
        tray.setNotification(Notifications.INFORMATION);
        tray.setAnimation(animation);
        tray.showAndWait();
    }

    public void sendMail(Course course) {
        Preference pref;
        PreferenceService preferenceService = new PreferenceServiceImpl();
        pref = preferenceService.getPreferences();

        try {
            Email email = new SimpleEmail();
            email.setCharset(EmailConstants.UTF_8);
            email.setHostName(pref.getPrefMailerHostname());
            email.setSmtpPort(pref.getPrefMailerPort());
            email.setAuthenticator(new DefaultAuthenticator(pref.getPrefMailerUsername(), pref.getPrefMailerPassword()));
            email.setSSLOnConnect(pref.getPrefMailerSSL());
            email.setFrom(pref.getPrefMailerFrom());
            email.setSubject("eGrades: Νέα βαθμολογία!");
            email.setMsg("Μάθημα: " + course.getCourseTitle() + " Βαθμός: " + course.getCourseGrade());
            email.addTo(pref.getPrefMailerTo());
            email.send();
        } catch (EmailException e) {
            exceptionService.showException(e, "Αποτυχία αποστολής βαθμολογίας με Email.");
        }
    }

    public void sendSampleMail(String hostname, String port, String username, String password, Boolean ssl, String fromMail, String toMail) {
        try {
            Email email = new SimpleEmail();
            email.setCharset(EmailConstants.UTF_8);
            email.setHostName(hostname);
            email.setSmtpPort(Integer.parseInt(port));
            email.setAuthenticator(new DefaultAuthenticator(username, password));
            email.setSSLOnConnect(ssl);
            email.setFrom(fromMail);
            email.setSubject("eGrades: Email Test!");
            email.setMsg("Αυτό είναι ένα δοκιμαστικό email. Εάν έχετε ενεργοποιημένο τον Mailer θα λαμβάνετε τις βαθμολογίες σας και αυτή τη διεύθυνση.");
            email.addTo(toMail);
            email.send();
        } catch (EmailException e) {
            exceptionService.showException(e, "Αποτυχία αποστολής δοκιμαστικού email.");
        }
    }
}
