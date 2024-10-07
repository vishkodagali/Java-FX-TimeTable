package assignment.assignment2_javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class styleController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private ImageView loginImage1;

    @FXML
    private ImageView loginImage4;

    @FXML
    private Label nameLabel1;

    @FXML
    private Label nameLabel11;

    @FXML
    private TextField newFontSizeTextField;

    @FXML
    private TextField newFontTextField;

    @FXML
    private Label setFirstNameLabel;

    @FXML
    private Label setStudentIDLabel;

    @FXML
    private Label studentIDLabel;

    @FXML
    private Label usernameLabel;

    private Scene scene;

    public void setScene(Scene scene) {
        this.scene = scene;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SqlConnection.setStudentInfo(SqlConnection.loggedStudent.get(0), usernameLabel, setStudentIDLabel, setFirstNameLabel);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SqlConnection.changeScene(event, "loggedIn.fxml", "Welcome to MyTimeTable!", SqlConnection.loggedStudent.get(0));
            }
        });

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SqlConnection.changeScene(event, "sample.fxml", "Welcome to MyTimeTable!", SqlConnection.loggedStudent.get(0));
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });



    }


}
