/*
 * Copyright (c) 2016.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 */

package gr.roropoulos.egrades.controller;

import com.github.plushaze.traynotification.animations.Animations;
import com.github.plushaze.traynotification.notification.Notifications;
import gr.roropoulos.egrades.eGrades;
import gr.roropoulos.egrades.model.Course;
import gr.roropoulos.egrades.model.Preference;
import gr.roropoulos.egrades.model.Student;
import gr.roropoulos.egrades.scheduler.SyncScheduler;
import gr.roropoulos.egrades.service.ExceptionService;
import gr.roropoulos.egrades.service.NotificationService;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.StudentService;
import gr.roropoulos.egrades.task.StudentTasks;
import gr.roropoulos.egrades.util.CourseListHelper;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.StatusBar;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Course> coursesTableView, lastRegTableView, recentTableView;
    @FXML
    private TableColumn<Course, String> idCoursesTableColumn, titleCoursesTableColumn, gradeCoursesTableColumn, creditsCoursesTableColumn,
            hoursCoursesTableColumn, ectsCoursesTableColumn, examDateCoursesTableColumn, semesterCoursesTableColumn,
            regCourseIdTableColumn, regCourseTitleTableColumn, regCourseGradeTableColumn, regCourseSemesterTableColumn,
            regCourseCreditsTableColumn, regCourseHoursTableColumn, recentCourseIdTableColumn, recentCourseTitleTableColumn,
            recentCourseGradeTableColumn, recentCourseCreditsTableColumn, recentCourseHoursTableColumn, recentCourseEctsTableColumn,
            recentCourseExamDateTableColumn, recentCourseSemesterTableColumn;
    private StatusBar statusBar;
    private Label statusLabel, studentNameStatusLabel, studentAEMStatusLabel, coursesStatusLabel, gradeStatusLabel,
            creditsStatusLabel, hoursStatusLabel, ectsStatusLabel;
    @FXML
    private Button syncButton;
    @FXML
    private ImageView syncImageView;
    private eGrades mainApp;
    private static MainController instance;
    private RotateTransition syncButtonRotateTransition;

    private final PreferenceService preferenceService = new PreferenceService();
    private final StudentService studentService = new StudentService();

    public MainController() {
        instance = this;
    }

    public static MainController getInstance() {
        return instance;
    }

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        Platform.setImplicitExit(false);
        Preference prefs = preferenceService.getUserPreferences();
        createStatusBar();
        setColorOnAllGradeCells();

        if (studentService.checkIfStudentExists()) {
            updateAllViewComponents();
            autoSync(prefs);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("eGrades");
            alert.setHeaderText("Πρώτη εκτέλεση του eGrades!");
            alert.setContentText(
                    "Για να ξεκινήσετε πληκτρολογήστε τα στοιχεία σας στην φόρμα που θα ακολουθήσει μετά απο αυτή " +
                            "ώστε να γίνει η ταυτοποιήση με την γραμματεία σας."
            );
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == okButton) {
                Platform.runLater(() -> mainApp.showAuthView());
            }
        }
    }

    private void autoSync(Preference prefs) {
        SyncScheduler syncScheduler = SyncScheduler.getInstance();
        if (prefs.getPrefSyncEnabled())
            syncScheduler.startSyncScheduler(prefs.getPrefSyncTime());
    }

    void updateAllViewComponents() {
        setColorOnAllGradeCells();
        updateStatusBar();
        setTableViewReg();
        setTableViewCourses();
        setTableViewRecent();
    }

    void clearCourseData() {
        recentTableView.getItems().clear();
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

    private void setColorOnAllGradeCells() {
        List<TableColumn<Course, String>> tableColumnList = new ArrayList<>();
        tableColumnList.add(regCourseGradeTableColumn);
        tableColumnList.add(gradeCoursesTableColumn);
        tableColumnList.add(recentCourseGradeTableColumn);

        for (TableColumn<Course, String> tableColumn : tableColumnList) {
            tableColumn.setCellFactory(column -> new TableCell<Course, String>() {
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
            });
        }
    }

    private void createStatusBar() {
        statusBar = new StatusBar();
        statusBar.setText("");
        statusBar.setMaxHeight(20);
        statusLabel = new Label("Σε αναμονή");
        statusBar.getLeftItems().add(statusLabel);

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
        HashMap<String, String> studentInfo = studentService.getStudentInfo();
        HashMap<String, String> studentStats = studentService.getStudentOverallStats();
        if (studentInfo != null && studentStats != null) {
            studentNameStatusLabel.setText(studentInfo.get("studentName") + " " + studentInfo.get("studentSurname"));
            studentAEMStatusLabel.setText(" " + "(" + studentInfo.get("studentAEM") + ")");
            coursesStatusLabel.setText(studentStats.get("sumCourses"));
            gradeStatusLabel.setText(studentStats.get("averageGrade"));
            creditsStatusLabel.setText(studentStats.get("sumCredits"));
            hoursStatusLabel.setText(studentStats.get("sumHours"));
            ectsStatusLabel.setText(studentStats.get("sumECTS"));
            borderPane.setBottom(statusBar);
        }
    }

    private ObservableList<Course> getInitialCourseTableData() {
        List<Course> courseList = studentService.getStudentCourses();
        if (courseList == null) {
            return FXCollections.observableList(new ArrayList<>());
        } else return FXCollections.observableList(courseList);
    }

    private ObservableList<Course> getInitialRegTableData() {
        List<Course> regList = studentService.getStudentRegister();
        if (regList == null) {
            return FXCollections.observableList(new ArrayList<>());
        } else return FXCollections.observableList(regList);
    }

    private ObservableList<Course> getInitialRecentTableData() {
        List<Course> recentList = studentService.getStudentRecentCourses();
        if (recentList == null) {
            return FXCollections.observableList(new ArrayList<>());
        } else return FXCollections.observableList(recentList);
    }

    private void setTableViewReg() {
        ObservableList<Course> dataReg = getInitialRegTableData();
        if (!dataReg.isEmpty()) {
            lastRegTableView.setItems(dataReg);
            regCourseIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
            regCourseTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
            regCourseGradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
            regCourseCreditsTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredits"));
            regCourseHoursTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseHours"));
            regCourseSemesterTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseSemester"));
            lastRegTableView.getSortOrder().add(regCourseIdTableColumn);
        }
    }

    private void setTableViewCourses() {
        ObservableList<Course> dataCourses = getInitialCourseTableData();
        if (!dataCourses.isEmpty()) {
            coursesTableView.setItems(dataCourses);
            idCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
            titleCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
            gradeCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
            creditsCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredits"));
            hoursCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseHours"));
            ectsCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseECTS"));
            examDateCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseExamDate"));
            semesterCoursesTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseSemester"));
            coursesTableView.getSortOrder().add(idCoursesTableColumn);
        }
    }

    private void setTableViewRecent() {
        ObservableList<Course> dataRecent = getInitialRecentTableData();
        if (!dataRecent.isEmpty()) {
            recentTableView.setItems(dataRecent);
            recentCourseIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
            recentCourseTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
            recentCourseGradeTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseGrade"));
            recentCourseCreditsTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseCredits"));
            recentCourseHoursTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseHours"));
            recentCourseEctsTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseECTS"));
            recentCourseExamDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseExamDate"));
            recentCourseSemesterTableColumn.setCellValueFactory(new PropertyValueFactory<>("courseSemester"));
        }
    }

    @FXML
    public void syncToolBarButtonAction() {
        if (studentService.checkIfStudentExists()) {
            syncButton.setDisable(true);
            startSyncButtonAnimation();
            Student student = studentService.getStudent();
            Integer timeout = preferenceService.getUserPreferences().getPrefAdvancedTimeout();
            StudentTasks studentTasks = new StudentTasks(student, timeout);
            Task<Map<String, String>> getCookieTask = studentTasks.getCookiesTask;

            getCookieTask.setOnSucceeded(e -> syncCourses(getCookieTask.getValue(), timeout));
            getCookieTask.setOnFailed(e -> stopSyncButtonAnimation());

            Thread t = new Thread(getCookieTask);
            t.setDaemon(true);
            t.start();
        }
    }

    @FXML
    private void changeStudentToolBarButton() {
        mainApp.showAuthView();
    }

    @FXML
    private void preferencesToolBarButton() {
        mainApp.showPrefView();
    }

    @FXML
    private void githubToolBarButton() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/pror21/eGrades"));
        } catch (IOException e) {
            ExceptionService.showException(e, "Δημιουργήθηκε IO exception.");
        } catch (URISyntaxException e) {
            ExceptionService.showException(e, "Λάθος σύνταξη URI.");
        }
    }

    @FXML
    private void exitToolBarButton() {
        if (preferenceService.getUserPreferences().getPrefShowCloseAlert())
            showCloseAlert();
        else
            mainApp.getPrimaryStage().hide();
    }

    @FXML
    private void deleteRecentToolBarButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("eGrades");
        alert.setHeaderText("Διαγραφή πρόσφατων βαθμολογιών!");
        alert.setContentText(
                "Επιβεβαίωση διαγραφής όλων των πρόσφατων βαθμολογιών."
        );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            recentTableView.getItems().clear();
            studentService.deleteStudentRecentCourses();
        }
    }

    private void showCloseAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Υπενθύμιση");
        alert.setHeaderText("Επιβεβαίωση διακοπής.");
        alert.setContentText("Η εφαρμογή θα συνεχίσει να τρέχει στο παρασκήνιο \r\n" +
                "και θα λαμβάνετε τυχόν ειδοποιήσεις.\r\n"
                + "\r\n" +
                "Για την απενεργοποίηση αυτής της υπενθύμισης ανατρέξτε στις ρυθμίσεις.\r\n");
        ButtonType stopButton = new ButtonType("Διακοπή");
        ButtonType okButton = new ButtonType("Εντάξει", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(stopButton, okButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton)
            mainApp.getPrimaryStage().hide();
        else if (result.get() == stopButton)
            mainApp.stopApplication();
    }

    private void notifyGrade(List<Course> newCourses) {
        NotificationService notificationService = new NotificationService();
        Preference pref = preferenceService.getUserPreferences();

        // Play notification sound if enabled
        if (pref.getPrefNotificationSoundEnabled())
            notificationService.playSoundNotification(pref.getPrefNotificationSound());

        // Show notification if enabled
        if (pref.getPrefNotificationPopupEnabled()) {
            Animations animation;
            switch (pref.getPrefNotificationPopupAnimation()) {
                case "popup":
                    animation = Animations.POPUP;
                    break;
                case "slide":
                    animation = Animations.SLIDE;
                    break;
                case "fade":
                    animation = Animations.FADE;
                    break;
                default:
                    animation = Animations.POPUP;
            }

            for (Course course : newCourses) {
                float val = Float.parseFloat(course.getCourseGrade().replace(',', '.'));
                if (val >= 5) {
                    notificationService.showNotification(course.getCourseTitle(),
                            "Πέρασες το μάθημα με βαθμό " + course.getCourseGrade(), Notifications.SUCCESS, animation);
                } else {
                    notificationService.showNotification(course.getCourseTitle(),
                            "Κόπηκες με βαθμό " + course.getCourseGrade(), Notifications.ERROR, animation);
                }
            }
        }

        // Send email if enabled
        if (pref.getPrefMailerEnabled()) {
            newCourses.forEach(notificationService::sendMail);
        }
    }

    private void syncCourses(Map<String, String> cookieJar, Integer timeout) {
        Student student = studentService.getStudent();

        StudentTasks studentTasks = new StudentTasks(student, cookieJar, timeout);
        Task<List<Course>> parseGradesTask = studentTasks.parseCoursesTask;
        Task parseStatsTask = studentTasks.parseAndSerializeStatsTask;
        Task parseRegTask = studentTasks.parseAndSerializeRegisterTask;

        Task<List<Course>> fetchNewGradesTask = new Task<List<Course>>() {
            @Override
            public List<Course> call() {
                List<Course> newList = null;
                try {
                    newList = parseGradesTask.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return CourseListHelper.compareCourseListsAndGetNewlyAnnouncementCourses(newList, student.getStudentCourses());
            }
        };

        fetchNewGradesTask.setOnSucceeded(e -> {
            List<Course> newlyListedGradeList = fetchNewGradesTask.getValue();
            if (!newlyListedGradeList.isEmpty()) {
                notifyGrade(newlyListedGradeList);
                studentService.insertStudentRecentCourses(newlyListedGradeList);
            }
            studentService.setStudentCourses(parseGradesTask.getValue());

            stopSyncButtonAnimation();
            updateAllViewComponents();
        });

        fetchNewGradesTask.setOnFailed(e -> stopSyncButtonAnimation());

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(parseGradesTask);
        es.submit(parseStatsTask);
        es.submit(parseRegTask);
        es.submit(fetchNewGradesTask);
        es.shutdown();

    }

    private void startSyncButtonAnimation() {
        syncButtonRotateTransition = new RotateTransition(Duration.millis(3000), syncImageView);
        syncButtonRotateTransition.setByAngle(360);
        syncButtonRotateTransition.setCycleCount(Animation.INDEFINITE);
        syncButtonRotateTransition.setInterpolator(Interpolator.LINEAR);
        syncButtonRotateTransition.play();
    }

    private void stopSyncButtonAnimation() {
        if (syncButtonRotateTransition != null) {
            if (syncButtonRotateTransition.getStatus() == Animation.Status.RUNNING) {
                syncButtonRotateTransition.stop();
                syncImageView.setRotate(0);
                syncButton.setDisable(false);
            }
        }
    }

    public void setCountdownTimeLabel(int hours, int minutes, int seconds) {
        statusLabel.setText("Επόμενος συγχρονισμός σε: " + hours + ":" + minutes + ":" + seconds);
    }

    public void stopCountdownTimeLabel() {
        statusLabel.setText("Σε αναμονή");
    }

    public void setMainApp(eGrades mainApp) {
        this.mainApp = mainApp;
    }
}
