/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades;

import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import com.sun.org.apache.xml.internal.utils.res.StringArrayWrapper;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import gr.roropoulos.egrades.domain.Student;
import gr.roropoulos.egrades.domain.University;
import gr.roropoulos.egrades.parser.CardisoftParser;
import gr.roropoulos.egrades.parser.Impl.CardisoftParserImpl;
import gr.roropoulos.egrades.service.Impl.StudentServiceImpl;
import gr.roropoulos.egrades.service.StudentService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class eGrades extends Application {

    private static final Logger log = LoggerFactory.getLogger(eGrades.class);
    private static List<String> argsList;

    public static void main(String[] args) throws Exception {
        argsList = new ArrayList<String>(Arrays.asList(args));
        for (String s: args) {
            argsList.add(s);
        }
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        String fxmlFile = null;
        FXMLLoader loader = null;
        Parent rootNode = null;
        Scene scene = null;

        // [0] = width, [1] = height
        Integer[] windowSize = new Integer[2];

        StudentService studentService = new StudentServiceImpl();
        if(studentService.studentCheckIfExist()) {
            fxmlFile = "/fxml/MainView.fxml";
            windowSize[0] = 640; windowSize[1] = 520;
        }
        else {
            fxmlFile = "/fxml/AuthView.fxml";
            windowSize[0] = 450; windowSize[1] = 220;
        }

        loader = new FXMLLoader();
        rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        scene = new Scene(rootNode, windowSize[0], windowSize[1]);
        scene.getStylesheets().add("/styles/styles.css");
        stage.setTitle("eGrades " + eGrades.class.getPackage().getImplementationVersion());
        stage.setScene(scene);
        stage.getIcons().addAll(
                new Image("/images/icons/Icon_96x96.png"),
                new Image("/images/icons/Icon_48x48.png"),
                new Image("/images/icons/Icon_36x36.png"),
                new Image("/images/icons/Icon_32x32.png"),
                new Image("/images/icons/Icon_24x24.png"),
                new Image("/images/icons/Icon_16x16.png")
        );
        stage.setWidth(windowSize[0]);
        stage.setHeight(windowSize[1]);
        stage.setResizable(false);
        System.out.println();

        if(argsList.contains("-s")) {
            stage.hide();}
        else
            stage.show();
    }
}
