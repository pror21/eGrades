package gr.roropoulos.egrades.controller;

import gr.roropoulos.egrades.domain.Student;
import gr.roropoulos.egrades.domain.University;
import gr.roropoulos.egrades.service.Impl.PreferenceServiceImpl;
import gr.roropoulos.egrades.service.Impl.StudentServiceImpl;
import gr.roropoulos.egrades.service.Impl.UniversityServiceImpl;
import gr.roropoulos.egrades.service.PreferenceService;
import gr.roropoulos.egrades.service.StudentService;
import gr.roropoulos.egrades.service.UniversityService;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

public class AuthController implements Initializable
{
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @FXML
    private ChoiceBox uniChoiceBox;
    @FXML
    private Button cancelButton, okButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Student student = new Student();
    private UniversityService universityService = new UniversityServiceImpl();
    private PreferenceService preferenceService = new PreferenceServiceImpl();
    private StudentService studentService = new StudentServiceImpl();
    private List<University> uniList = universityService.getUniversitiesList();

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        // Sort University List Alphabetically
        uniList.sort((uni1, uni2) -> uni1.getUniversityName().compareTo(uni2.getUniversityName()));
        // Set ChoiceBox items from List
        uniChoiceBox.getItems().setAll(uniList);
        // Preselect the first item
        uniChoiceBox.getSelectionModel().select(0);
        // Initialize all Handlers and bindings
        initializeHandlers();
        loadStudentData();
    }

    @FXML
    protected void handleOkButtonAction(ActionEvent event) {
        student.setStudentUsername(usernameField.getText());
        student.setStudentPassword(passwordField.getText());
        studentService.studentSerialize(student);

        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void handleCancelButtonAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void initializeHandlers() {
        // Set Student university selection
        uniChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<University>() {
            @Override
            public void changed(ObservableValue<? extends University> observable, University oldValue, University newValue) {
                student.setStudentUniversity(newValue);
            }
        });

        // Require Username and Password for enabling OK Button
        BooleanBinding okButtonBooleanBind = usernameField.textProperty().isEmpty().or(passwordField.textProperty().isEmpty());
        okButton.disableProperty().bind(okButtonBooleanBind);
    }

    private void loadStudentData() {
        if(studentService.studentCheckIfExist()) {
            University selectedUni = uniList.stream()
                    .filter(item -> item.getUniversityName().equals(studentService.studentDeSerialize().getStudentUniversity().getUniversityName()))
                    .findFirst().get();
            uniChoiceBox.getSelectionModel().select(selectedUni);
            usernameField.setText(studentService.studentDeSerialize().getStudentUsername());
            passwordField.setText(studentService.studentDeSerialize().getStudentPassword());
        }
    }
}
