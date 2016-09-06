/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.eGrades;
import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.parser.Impl.StudentParserImpl;
import gr.roropoulos.egrades.parser.StudentParser;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.Impl.StudentServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.StudentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.StatusBar;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private eGrades mainApp;
    private static MainController instance;

    @FXML
    private BorderPane borderPane;

    @FXML
    private CheckMenuItem settingAutoSyncCheckMenuItem, settingNotificationsCheckMenuItem;

    @FXML
    private TableView coursesTableView, lastRegTableView;

    @FXML
    private TableColumn idCoursesTableColumn, titleCoursesTableColumn, gradeCoursesTableColumn, creditsCoursesTableColumn,
            hoursCoursesTableColumn, ectsCoursesTableColumn, examDateCoursesTableColumn, semesterCoursesTableColumn,
            regCourseIdTableColumn, regCourseITitleableColumn, regCourseGradeTableColumn, regCourseSemesterTableColumn,
            regCourseCreditsTableColumn, regCourseHoursTableColumn;

    // StatusBar components
    private ObservableList dataCourses;
    private ObservableList dataReg;
    private StatusBar statusBar;
    private ProgressIndicator progressIndicator;
    private Label statusLabel, studentNameStatusLabel, studentAEMStatusLabel, coursesStatusLabel, gradeStatusLabel,
            creditsStatusLabel, hoursStatusLabel, ectsStatusLabel;

    // Services & Repos
    private StudentParser studentParser = new StudentParserImpl();
    private StudentService studentService = new StudentServiceImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();

    public MainController() {
        instance = this;
    }

    public static MainController getInstance() {
        return instance;
    }

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        Platform.setImplicitExit(false);

        Preference pref = preferenceService.getPreferences();
        setUISettings(pref);
        setStatusBar();
        if (studentService.studentCheckIfExist()) {
            updateCourseData();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("eGrades");
            alert.setHeaderText("Φαίνεται πως είναι η πρώτη φορά που εκτελείτε την εφαρμογή eGrades!");
            alert.setContentText(
                    "Για να ξεκινήσετε πλοηγηθήτε στο μενού Αρχείο > Λογαριασμός Φοιτητή και πληκτρολογήστε τα στοιχεία σας " +
                            "ώστε να γίνει η ταυτοποιήση με την γραμματεία σας."
            );
            alert.show();
        }
    }

    public void updateCourseData() {
        setRegTableView();
        setCoursesTableView();
        updateStatusBar();
        loadTableViewReg();
        loadTableViewCourses();
    }

    public void clearCourseData() {
        lastRegTableView.getItems().clear();
        coursesTableView.getItems().clear();
        studentNameStatusLabel.setText("---");
        studentAEMStatusLabel.setText("---");
        coursesStatusLabel.setText("---");
        gradeStatusLabel.setText("---");
        creditsStatusLabel.setText("---");
        hoursStatusLabel.setText("---");
        ectsStatusLabel.setText("---");
    }

    private void setRegTableView() {
        regCourseGradeTableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<String, String>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            try {
                                float val = Float.parseFloat(item.replace(',', '.'));
                                if (val >= 5)
                                    this.setTextFill(Color.GREEN);
                                else if (val < 5)
                                    this.setTextFill(Color.RED);
                            } catch (NumberFormatException e) {
                                // shhhh.. :)
                            }
                        }
                        setText(item);
                    }
                };
            }
        });
    }

    private void setCoursesTableView() {
        gradeCoursesTableColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn param) {
                return new TableCell<String, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            try {
                                float val = Float.parseFloat(item.replace(',', '.'));
                                if (val >= 5)
                                    this.setTextFill(Color.GREEN);
                                else if (val < 5)
                                    this.setTextFill(Color.RED);
                            } catch (NumberFormatException e) {
                                // shhhh.. :)
                            }
                        }
                        setText(item);
                    }
                };
            }
        });
    }

    private void setStatusBar() {
        statusBar = new StatusBar();
        statusBar.setText("");
        statusBar.setMaxHeight(20);
        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        statusBar.getLeftItems().add(progressIndicator);
        statusBar.getLeftItems().add(new Label("Κατάσταση: "));
        statusLabel = new Label("Σε αναμονή");
        statusBar.getLeftItems().add(statusLabel);

        // student overall status stats
        Label courses = new Label(" Περασμένα: ");
        Label mo = new Label(" ΜΟ: ");
        Label cr = new Label(" ΔΜ: ");
        Label hrs = new Label(" Ώρες: ");
        Label ect = new Label(" ECTS: ");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        separator.setPadding(new Insets(0, 5, 0, 5));
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.setPadding(new Insets(0, 5, 0, 5));
        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);
        separator3.setPadding(new Insets(0, 5, 0, 5));
        Separator separator4 = new Separator();
        separator4.setOrientation(Orientation.VERTICAL);
        separator4.setPadding(new Insets(0, 5, 0, 5));
        Separator separator5 = new Separator();
        separator5.setOrientation(Orientation.VERTICAL);
        separator5.setPadding(new Insets(0, 5, 0, 5));

        studentNameStatusLabel = new Label("---");
        studentAEMStatusLabel = new Label("---");
        coursesStatusLabel = new Label("---");
        gradeStatusLabel = new Label("---");
        creditsStatusLabel = new Label("---");
        hoursStatusLabel = new Label("---");
        ectsStatusLabel = new Label("---");

        statusBar.getRightItems().addAll(
                studentNameStatusLabel,
                studentAEMStatusLabel,
                separator,
                courses, coursesStatusLabel,
                separator2,
                mo, gradeStatusLabel,
                separator3,
                cr, creditsStatusLabel,
                separator4,
                hrs, hoursStatusLabel,
                separator5,
                ect, ectsStatusLabel);
    }

    private void updateStatusBar() {
        HashMap<String, String> studentInfo = studentService.studentGetInfo();
        HashMap<String, String> studentStats = studentService.studentGetStats();

        studentNameStatusLabel.setText(studentInfo.get("studentName") + " " + studentInfo.get("studentSurname"));
        studentAEMStatusLabel.setText(" " + "(" + studentInfo.get("studentAEM") + ")");
        coursesStatusLabel.setText(studentStats.get("sumCourses"));
        gradeStatusLabel.setText(studentStats.get("averageGrade"));
        creditsStatusLabel.setText(studentStats.get("sumCredits"));
        hoursStatusLabel.setText(studentStats.get("sumHours"));
        ectsStatusLabel.setText(studentStats.get("sumECTS"));
        borderPane.setBottom(statusBar);
    }

    private void setUISettings(Preference pref) {
        if (pref.getSettingAutoSync()) settingAutoSyncCheckMenuItem.setSelected(true);
        if (pref.getSettingNotifications()) settingAutoSyncCheckMenuItem.setSelected(true);
    }

    private ObservableList getInitialCourseTableData() {
        List<Course> courseList = studentService.studentGetAllCourses();
        ObservableList data = FXCollections.observableList(courseList);
        return data;
    }

    private ObservableList getInitialRegTableData() {
        List<Course> regList = studentService.studentGetLastRegCourseList();
        ObservableList dataReg = FXCollections.observableList(regList);
        return dataReg;
    }

    private void loadTableViewReg() {
        dataReg = getInitialRegTableData();
        lastRegTableView.setItems(dataReg);
        regCourseIdTableColumn.setCellValueFactory(new PropertyValueFactory("courseId"));
        regCourseITitleableColumn.setCellValueFactory(new PropertyValueFactory("courseTitle"));
        regCourseGradeTableColumn.setCellValueFactory(new PropertyValueFactory("courseGrade"));
        regCourseCreditsTableColumn.setCellValueFactory(new PropertyValueFactory("courseCredits"));
        regCourseHoursTableColumn.setCellValueFactory(new PropertyValueFactory("courseHours"));
        regCourseSemesterTableColumn.setCellValueFactory(new PropertyValueFactory("courseSemester"));
        lastRegTableView.getSortOrder().add(regCourseIdTableColumn);
    }

    private void loadTableViewCourses() {
        dataCourses = getInitialCourseTableData();
        coursesTableView.setItems(dataCourses);
        idCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseId"));
        titleCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseTitle"));
        gradeCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseGrade"));
        creditsCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseCredits"));
        hoursCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseHours"));
        ectsCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseECTS"));
        examDateCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseExamDate"));
        semesterCoursesTableColumn.setCellValueFactory(new PropertyValueFactory("courseSemester"));
        coursesTableView.getSortOrder().add(idCoursesTableColumn);
    }

    @FXML
    private void syncMenuItemAction(ActionEvent event) {
        Student student = studentService.studentDeSerialize();
        studentService.studentSetAllCourses(studentParser.parseStudentGrades(student));
        studentService.studentSetStats(studentParser.parseStudentStats(student));
    }

    @FXML
    private void changeStudentMenuAction(ActionEvent event) {
        mainApp.showAuthView();
    }

    @FXML
    private void preferencesMenuAction(ActionEvent event) {
        mainApp.showPrefView();
    }

    @FXML
    private void exitMenuAction(ActionEvent event) {

        Platform.exit();
    }

    @FXML
    private void settingAutoSyncCheckMenuItemHandle(ActionEvent event) {
        Preference pref = preferenceService.getPreferences();
        if (settingAutoSyncCheckMenuItem.isSelected()) {
            pref.setSettingAutoSync(true);
        } else {
            pref.setSettingAutoSync(false);
        }
        preferenceService.setPreferences(pref);
    }

    @FXML
    private void settingNotificationsCheckMenuItemHandle(ActionEvent event) {
        Preference pref = preferenceService.getPreferences();
        if (settingNotificationsCheckMenuItem.isSelected()) {
            pref.setSettingNotifications(true);
        } else {
            pref.setSettingNotifications(false);
        }
        preferenceService.setPreferences(pref);
    }

    public void setMainApp(eGrades mainApp) {
        this.mainApp = mainApp;
    }
}
