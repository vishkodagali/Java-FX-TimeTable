package assignment.assignment2_javafx;

// Importing necessary JavaFX packages
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField usernameTextField; // Text field for username

    @FXML
    private TextField passwordText; // Text field for displaying password

    @FXML
    private Button loginButton; // Button for initiating the logging in

    @FXML
    private Button signUpButton; // Button to the sign-up page

    @FXML
    private PasswordField passwordTextField; // Password field for entering the password

    @FXML
    private CheckBox showCheckBox; // Checkbox for the visibility of the password

    @FXML
    private ImageView deleteImage; // Image view for displaying delete icon

    @FXML
    private ImageView enrollImage; // Image view for displaying an enrollment icon

    @FXML
    private ImageView loginImage; // Image view for displaying a icon

    @FXML
    private ImageView loginImage1; // Image view for displaying a icon

    @FXML
    private ImageView loginImage2; // Image view for displaying a icon

    @FXML
    private ImageView loginImage3; // Image view for displaying a icon

    @FXML
    private ImageView loginImage4; // Image view for displaying icon

    @FXML
    private ImageView viewImage; // Image view for displaying view icon

    /**
     * Event handler for changing the visibility of the password field based on the state of the showCheckBox.
     * If the showCheckBox is selected, the password will be displayed in a regular TextField,
     * otherwise, it will be displayed in a PasswordField.
     */

    @FXML
    void ChangeVisibility(ActionEvent event){
        //If the showCheckBox is clicked, display the password in Text Field.
        if (showCheckBox.isSelected()){
            passwordText.setText(passwordTextField.getText());
            passwordText.setVisible(true); // Show the password TextField.
            passwordTextField.setVisible(false); // Hide the password PasswordField.
            return;
        }
        // If the showCheckBox is not selected, display the password in a PasswordField.
        passwordTextField.setText(passwordText.getText());
        passwordTextField.setVisible(true);// Show the password PasswordField.
        passwordText.setVisible(false); //Hide the password text field.
    }


    /**
     * Login button and Signup button event
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Login button event.
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Calling login methode to handle student login.
                SqlConnection.loginStudent(event, usernameTextField.getText(), passwordTextField.getText());
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            // Signup button event
            @Override
            public void handle(ActionEvent event) {
                //Calling change scene method that will switch to the sign-up page to sign up students.
                SqlConnection.changeScene(event, "signUp.fxml", "Student sign up!",null);
            }
        });

    }
}
