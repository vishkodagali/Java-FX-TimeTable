package assignment.assignment2_javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class editProfileController implements Initializable {
    @FXML
    private Button cancelButton; // Button for canceling the current operation

    @FXML
    private Button confirmButton; // Button for confirming the current operation

    @FXML
    private Button logOutButton; // Button for logging out

    @FXML
    private ImageView loggedImage1; // Image view for displaying a logged-in user icon

    @FXML
    private ImageView loggedImage2; // Image view for displaying a logged-in user icon variant

    @FXML
    private ImageView loggedImage3; // Image view for displaying a logged-in user icon variant

    @FXML
    private ImageView loginImage1; // Image view for displaying a login icon

    @FXML
    private ImageView loginImage2; // Image view for displaying a login icon

    @FXML
    private ImageView loginImage3; // Image view for displaying a login icon

    @FXML
    private ImageView loginImage4; // Image view for displaying a login icon

    @FXML
    private TextField newFirstNameTextField; // Text field for entering a new first name

    @FXML
    private TextField newLastNameTextField; // Text field for entering a new last name

    @FXML
    private PasswordField newPasswordTextField; // Password field for entering a new password

    @FXML
    private TextField newPasswordText; // Text field for displaying the new password temporarily

    @FXML
    private CheckBox showCheckBox; // Checkbox for toggling the visibility of the new password

    @FXML
    private Rectangle rect; // Rectangle for visual purposes

    @FXML
    private Label setFirstNameLabel; // Label for displaying the current first name

    @FXML
    private Label setLastNameLabel; // Label for displaying the current last name

    @FXML
    private Label setStudentIDLabel; // Label for displaying the student ID

    @FXML
    private Label studentIDLabel; // Label for displaying the student ID value

    @FXML
    private Label firstNameLabel; // Label for displaying the first name

    @FXML
    private Label lastNameLabel; // Label for displaying the last name

    @FXML
    private Label usernameLabel; // Label for displaying the username

    /**
     * Event handler for changing the visibility of the password field based on the state of the showCheckBox.
     * If the showCheckBox is selected, the password will be displayed in a regular TextField,
     * otherwise, it will be displayed in a PasswordField.
     */

    @FXML
    void ChangeVisibility(ActionEvent event){
        // If the showCheckBox is selected, display the new password in the text field
        // and hide the password field
        if (showCheckBox.isSelected()){
            newPasswordText.setText(newPasswordTextField.getText());
            newPasswordText.setVisible(true);
            newPasswordTextField.setVisible(false);
            return;
        }
        // If the showCheckBox is not selected, display the new password in the password field
        // and hide the temporary text field
        newPasswordTextField.setText(newPasswordText.getText());
        newPasswordTextField.setVisible(true);
        newPasswordText.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the student information in the labels
        SqlConnection.setStudentInfo(SqlConnection.loggedStudent.get(0), usernameLabel, setStudentIDLabel, setFirstNameLabel);
        // Creating a tooltip for the cancel button
        Tooltip cancelTip = new Tooltip("Click on Cancel to Cancel or to Go back to Dashboard!");
        cancelButton.setTooltip(cancelTip); //tool tip to provide info to the user.

        // Handle the cancel button click event
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Calling change scene method that will switch to the student dashboard page.
                SqlConnection.changeScene(event, "loggedIn.fxml", "Welcome to Student Enrollment!", SqlConnection.loggedStudent.get(0));
            }
        });

        // Handle the logout button click event.
        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // creation of new alert of confirmation for logout
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Logout");
                alert.setContentText("Are you sure you want to logout?");
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);
                // Check the student's choice and handle accordingly
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yesButton) {
                        // Clear the logged-in student arraylist information and change the scene to the login screen
                        SqlConnection.loggedStudent.clear();
                        SqlConnection.changeScene(event, "sample.fxml", "Student login!",null);
                    }
                });
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Creating a confirmation dialog alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Edit Profile");
                alert.setContentText("Are you sure you want to Change your details?");
                // creating new buttons for Alert
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);

                // Show the confirmation dialog and handle the student's choice
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yesButton) {
                        // If the student chooses "Yes", update the student profile, refresh the information, and clear the input fields
                        editProfile.editStudentProfile(SqlConnection.loggedStudent.get(0), newFirstNameTextField, newLastNameTextField,newPasswordTextField);
                        SqlConnection.setStudentInfo(SqlConnection.loggedStudent.get(0), usernameLabel, setStudentIDLabel, setFirstNameLabel);
                        newFirstNameTextField.clear();
                        newPasswordTextField.clear();
                        newLastNameTextField.clear();
                        newPasswordText.clear();
                    }
                });
            }
        });
    }
}
