/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.scheduler;

import gr.roropoulos.egrades.controller.MainController;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class SyncScheduler {
    private static SyncScheduler instance = null;

    private SyncScheduler() {
    }

    public static SyncScheduler getInstance() {
        if (instance == null) {
            instance = new SyncScheduler();
        }
        return instance;
    }

    private Timer schedulerTimer;
    private Timer countdownTimer;

    public void startSyncScheduler(int rate) {
        schedulerTimer = new Timer();
        startCountdownLabel(rate);
        schedulerTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                startCountdownLabel(rate);
                MainController.getInstance().syncToolBarButtonAction();
            }
        }, rate * 60 * 1000, rate * 60 * 1000);
    }

    private void startCountdownLabel(int rate) {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            int n = rate * 60;

            @Override
            public void run() {
                int hoursLeft = (n / 3600);
                int minutesLeft = (n / 60) - (hoursLeft * 60);
                int secondsLeft = (n) - ((minutesLeft * 60) + (hoursLeft * 60 * 60));
                Platform.runLater(() -> MainController.getInstance().setCountdownTimeLabel(hoursLeft, minutesLeft, secondsLeft));
                n--;
                if (n <= 0)
                    countdownTimer.cancel();
            }
        }, 0, 1000);
    }

    public void stopScheduler() {
        if (schedulerTimer != null && countdownTimer != null) {
            schedulerTimer.cancel();
            schedulerTimer.purge();
            countdownTimer.cancel();
            countdownTimer.purge();
            Platform.runLater(() -> MainController.getInstance().stopCountdownTimeLabel());
        }
    }
}
