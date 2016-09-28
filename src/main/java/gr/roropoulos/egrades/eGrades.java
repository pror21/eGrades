/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades;

import com.sun.imageio.plugins.common.ImageUtil;
import gr.roropoulos.egrades.controller.AuthController;
import gr.roropoulos.egrades.controller.MainController;
import gr.roropoulos.egrades.controller.PrefController;
import gr.roropoulos.egrades.service.ExceptionService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.*;
import java.util.List;

public class eGrades extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ExceptionService exceptionService = new ExceptionService();
    private Properties properties = new Properties();
    private static ServerSocket SERVER_SOCKET;

    public static void main(String[] args) throws Exception {
        try {
            SERVER_SOCKET = new ServerSocket(43430);
        } catch (IOException x) {
            System.out.println("Another instance already running... exit.");
            System.exit(1);
        }

        List<String> argsList = new ArrayList<>(Arrays.asList(args));
        Collections.addAll(argsList, args);
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));

        this.primaryStage = stage;
        this.primaryStage.setTitle(properties.getProperty("artifactId") + " " + properties.getProperty("version"));
        primaryStage.getIcons().addAll(
                new Image("/images/icons/Icon_96x96.png"),
                new Image("/images/icons/Icon_48x48.png"),
                new Image("/images/icons/Icon_36x36.png"),
                new Image("/images/icons/Icon_32x32.png"),
                new Image("/images/icons/Icon_24x24.png"),
                new Image("/images/icons/Icon_16x16.png")
        );

        initRootLayout();
        showMainView();
        setTray();

    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException e) {
            exceptionService.showException(e, "Δεν βρέθηκε το Root Layout.");
        }
    }

    private void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/MainView.fxml"));
            BorderPane mainView = loader.load();
            rootLayout.setCenter(mainView);
            MainController mainController = loader.getController();
            mainController.setMainApp(this);
        } catch (IOException e) {
            exceptionService.showException(e, "Δεν βρέθηκε το FXML MainView.");
        }
    }

    public void showAuthView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/AuthView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.initOwner(primaryStage);
            dialogStage.setTitle("Είσοδος χρήστη");
            dialogStage.setWidth(450);
            dialogStage.setHeight(220);
            dialogStage.getIcons().add(new Image("/images/icons/Icon_32x32.png"));
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AuthController authController = loader.getController();
            authController.setDialogStage(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            exceptionService.showException(e, "Δεν βρέθηκε το FXML AuthfView.");
        }
    }

    public void showPrefView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/PrefView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.initOwner(primaryStage);
            dialogStage.setTitle("Προτιμήσεις");
            dialogStage.setWidth(420);
            dialogStage.setHeight(260);
            dialogStage.getIcons().add(new Image("/images/icons/Icon_32x32.png"));
            dialogStage.setResizable(false);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PrefController prefController = loader.getController();
            prefController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopApplication() {
        Platform.exit();
        System.exit(0);
    }

    private void showStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    private void setTray() {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                PopupMenu popup = new PopupMenu();
                URL url = ImageUtil.class.getResource("/images/icons/Icon_16x16.png");
                java.awt.Image image = ImageIO.read(url);
                TrayIcon trayIcon = new TrayIcon(image, "eGrades", popup);
                trayIcon.addActionListener(event -> Platform.runLater(this::showStage));
                MenuItem exitItem = new MenuItem("Exit");
                exitItem.addActionListener(event -> {
                    tray.remove(trayIcon);
                    stopApplication();
                });
                popup.add(exitItem);
                tray.add(trayIcon);
            }
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
