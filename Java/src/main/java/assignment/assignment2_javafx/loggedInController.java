package assignment.assignment2_javafx;

// Importing necessary JavaFx packages
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * This controller class has all the necessary attributes like images, text fields, list views and buttons that allow the student to arrive at the dashboard
 * This class has several main buttons that allow the user to navigate to edit profile, enroll, withdraw, change theme, and MyTimeTable view for timetable.
 */
public class loggedInController implements Initializable {

    @FXML
    private Label IdLabel; // Label for ID

    @FXML
    private Label usernameLabel; // Label for username

    @FXML
    private Button editProfileButton; // Button for editing profile

    @FXML
    private Button enrollButton; // Button for enrollment

    @FXML
    private Button logOutButton; // logout button

    @FXML
    private ImageView loggedImage1; // Image icon

    @FXML
    private ImageView loggedImage2; // Image background

    @FXML
    private ImageView loggedImage3; // Icon Image

    @FXML
    private ImageView loggedImage4; // Image background

    @FXML
    private ImageView loggedImage5; // Image for Icon

    @FXML
    private Label nameLabel; // label for name

    @FXML
    private Button viewEnrollmentButton; // button for View enrollment

    @FXML
    private Button editStyleButton; // Button to edit style




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        // Method called to set the student information
        SqlConnection.setStudentInfo(SqlConnection.loggedStudent.get(0), usernameLabel, IdLabel, nameLabel);

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { // // Event handler for the logOutButton click event.
                // Creating an Alert for confirmation logout
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Logout!");
                alert.setContentText("Are you sure you want to logout?");
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yesButton) {
                        // Clears the loggedStudent list and switch the scene to the login page.
                        SqlConnection.loggedStudent.clear();
                        SqlConnection.changeScene(event, "sample.fxml", "Student login!", null);
                    }
                });
            }
        });

        editProfileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Event for the edit Profile Button.
                SqlConnection.changeScene(event, "editProfile.fxml", "Edit Profile!", SqlConnection.loggedStudent.get(0));
                // Switching the scene to the edit profile fxml by calling the changeScene method of the SqlConnection class
                // by passing the event, FXML file name, title, and the logged student's username as parameters.
            }
        });


        enrollButton.setOnAction(new EventHandler<ActionEvent>() {
            // Event handler for the enrollButton click event.
            @Override
            public void handle(ActionEvent event) {
                SqlConnection.changeScene(event, "enroll.fxml", "Welcome to Student Enrollment!", SqlConnection.loggedStudent.get(0));
            }
        });

        viewEnrollmentButton.setOnAction(new EventHandler<ActionEvent>() {
            // Event handler for the view enrollment event.
            @Override
            public void handle(ActionEvent event) {
               SqlConnection.changeScene(event, "timeTableView.fxml", "Welcome To MyTimeTable View!",SqlConnection.loggedStudent.get(0));
                // Switching the scene to the enrollment fxml by calling the changeScene method
                // by passing the event, FXML file name, title, and the logged student's username as parameters.

            }
        });

        editStyleButton.setOnAction(new EventHandler<ActionEvent>() {
            // Event handler for the editStyleButton event.
            @Override
            public void handle(ActionEvent event) {
                SqlConnection.changeScene(event, "changeFont.fxml", "Welcome to Edit Style!", SqlConnection.loggedStudent.get(0));
                // Changing the scene to the changeFont fxml file by calling the changeScene method of the SqlConnection class.
                // Passes the event, FXML file name, title, and the logged student's username as parameters.

            }
        });



    }

}
