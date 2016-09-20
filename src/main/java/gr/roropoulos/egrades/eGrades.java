/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades;

import gr.roropoulos.egrades.controller.AuthController;
import gr.roropoulos.egrades.controller.MainController;
import gr.roropoulos.egrades.controller.PrefController;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.Impl.SerializeServiceImpl;
import gr.roropoulos.egrades.service.SerializeService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class eGrades extends Application {

    private static final Logger log = LoggerFactory.getLogger(eGrades.class);
    private static List<String> argsList;
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ExceptionService exceptionService = new ExceptionService();
    private SerializeService serializeService = new SerializeServiceImpl();

    private MainController mainController;

    public static void main(String[] args) throws Exception {
        argsList = new ArrayList<String>(Arrays.asList(args));
        for (String s : args) {
            argsList.add(s);
        }
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.primaryStage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion());
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
        //TODO: START IN STEALTH
        //if (argsList.contains("-s")){}
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(eGrades.class.getResource("/fxml/MainView.fxml"));
            BorderPane mainView = loader.load();
            rootLayout.setCenter(mainView);
            mainController = loader.getController();
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
            dialogStage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion() + " - Αλλαγή χρήστη");
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
            dialogStage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion() + " - Προτιμήσεις");
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

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
