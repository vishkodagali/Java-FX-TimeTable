package assignment.assignment2_javafx;

// Importing necessary JavaFx packages
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is a signup controller that allows the students to sign up with unique credentials
 * This controller initializes several attributes and button on action.
 * Buttons are set to log in the student or sign them up, there are necessary method calls to switch the scenes.
 */

public class signUpController implements Initializable {

    @FXML
    private TextField usernameTextField; // Text field for entering the username.

    @FXML
    private TextField passwordText; // Text field for entering the password

    @FXML
    private TextField firstNameTextField; // Text field for entering the first name

    @FXML
    private TextField lastNameTextField; // Text field for entering the last name

    @FXML
    private TextField studentIdTextField; // Text field for entering the student ID

    @FXML
    private Button signUpButton; // Button for initiating the signup.

    @FXML
    private Button loginButton; // Button for initiating the login.

    @FXML
    private PasswordField passwordTextField; // Password field for entering the password

    @FXML
    private CheckBox showCheckBox; // Check box for visibility of the password text field.

    @FXML
    private ImageView signUpImage; // Image view for displaying an image

    @FXML
    private ImageView signUpImage1; // Image view for displaying an image

    @FXML
    private ImageView signUpImage2; // Image view for displaying an image


    @FXML
    void ChangeVisibility(ActionEvent event){
        if (showCheckBox.isSelected()){
            // Setting the text of the visible password text field to the entered password.
            passwordText.setText(passwordTextField.getText());  // Making the visible password text field visible.
            passwordText.setVisible(true); // Hiding the password text field used for input.
            passwordTextField.setVisible(false);
            return;
        }
        passwordTextField.setText(passwordText.getText()); // Setting the text of the password text field used for input to the entered password.
        passwordTextField.setVisible(true); // Making the password text field used for input visible.
        passwordText.setVisible(false);   // Hide the visible password text field.
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Checking if the username and password fields are not empty.
                if (!usernameTextField.getText().trim().isEmpty() && !passwordTextField.getText().trim().isEmpty()){
                    // Calling the signU Student method
                    SqlConnection.signUpStudent(event, usernameTextField.getText().trim(), passwordTextField.getText(), firstNameTextField.getText().trim(), lastNameTextField.getText().trim(),studentIdTextField.getText().trim());
                }else {
                    //System.err.println("Please Fill in the All the required information");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please Fill in the All the required information");
                    alert.show();
                    // Show an error alert with the message.
                }
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // switch the scene to the sample.fxml file for student login.
                SqlConnection.changeScene(event,"sample.fxml", "Student LogIn!", null);
            }
        });

    }
}
