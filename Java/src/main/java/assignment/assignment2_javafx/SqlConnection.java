package assignment.assignment2_javafx;

// Importing necessary JavaFx packages
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

/**
 * This is a Database function class where the sign in and log in methods are constructed.
 * The login student method allows the student to log in with unique credentials with password hash.
 * The signup method allows the student to signup for the first time, with several name fields and password hash.
 * The set student info method will set the necessary information upon called with necessary inputs.
 */


class SqlConnection {

    // Creating array list to store the logged in student to use it in Set student info method
    static ArrayList<String> loggedStudent = new ArrayList<String>();

    // Method to change the scene
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username){
        Parent root = null;
        if(username != null){
            try{
                // Loading the FXML file and assign it to the root variable using try catch.
                FXMLLoader loader = new  FXMLLoader(SqlConnection.class.getResource(fxmlFile));
                root = loader.load();
            }catch (IOException t){
                t.printStackTrace();
            }
            // Changing the scene to timeTableView.fxml with a desirable size.
            if (fxmlFile.equals("timeTableView.fxml")){
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle(title);
                stage.setScene(new Scene(root, 850,850));
                stage.show();
            }else if (fxmlFile.equals("enroll.fxml")) {
                // Change the scene to enroll.fxml with a desirable size.
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle(title);
                stage.setScene(new Scene(root, 800,800));
                stage.show();
            }else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle(title);
                stage.setScene(new Scene(root, 600,600));
                stage.show(); // Change the scene to a default FXML file with a specific size.
            }

        }else {
            try{
            root = FXMLLoader.load(SqlConnection.class.getResource(fxmlFile));  // Load the FXML file and assign it to the root variable.
        }catch (IOException t){
            t.printStackTrace();
        }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 600,600));
            stage.show();
            // Change the scene to the specified FXML file.

        }
    }

    // Method to sign up new student
    public static void signUpStudent(ActionEvent e, String username, String password, String firstName, String lastName, String studentNumber){
        Connection connection = null; // Establishing the connection to db declaration
        PreparedStatement psInsert = null; // declaration of prepared statement to insert
        PreparedStatement psCheckStudentExists = null; // declaration of a prepared statement to check if the user exists
        ResultSet resultSet = null; // declaration of  a result set to iterate.
        try{
            // connecting to db
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db");
            psCheckStudentExists = connection.prepareStatement("SELECT * FROM Student WHERE username = ?"); // select query for checking if the user exists
            psCheckStudentExists.setString(1, username);
            resultSet = psCheckStudentExists.executeQuery();
            if (resultSet.isBeforeFirst()){
                // checking if the student username is already available in the db
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
                // showing alert if the student duplicate occur
            }else {
                // setting the criteria for the length of the username and other variables.
                if (username.length() >= 3 && password.length() >= 3 && firstName.length() >= 3 && studentNumber.length() >= 3){
                    psInsert = connection.prepareStatement("INSERT INTO Student (username, password, firstName, lastName, studentNumber) VALUES (?,?,?,?,?)");
                    psInsert.setString(1, username);
                    psInsert.setString(2, hashCryptPassword(password));
                    psInsert.setString(3, firstName);
                    psInsert.setString(4, lastName);
                    psInsert.setString(5, studentNumber);
                    psInsert.executeUpdate(); // executing the SQL statement
                    changeScene(e, "sample.fxml", "Student Login!",username); // switching the scene to next level
                    Alert alert = new Alert(Alert.AlertType.INFORMATION); // showing an alert to welcome student for the first time
                    alert.setHeaderText("Welcome! "+username);
                    alert.setContentText("Your student profile has been successfully created with student Id: "+studentNumber+
                            "\nPlease login to continue");
                    alert.show();
                    // Alert show if the new student is created.
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Fields should have 3 characters");
                    alert.show();
                    // showing alert if the characters are less than 3
                }
            }
        }catch (SQLException t){
            // Handling the exception for SQl
            t.printStackTrace();
        }finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (psCheckStudentExists != null){
                try{
                    psCheckStudentExists.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (connection != null){
                try{
                    connection.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
        }

    }

    // Method to has the password
    public static String hashCryptPassword(String password) {
        try {
            // Creating a new message to get the SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            // defining the has bytes using the byte
            StringBuilder hash = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            return hash.toString(); // returning the hashed outcome
        } catch (NoSuchAlgorithmException e) {
            // handling exception
            e.printStackTrace();
        }

        return null;
    }

    // Method to log in the student
    public static void loginStudent(ActionEvent e, String username, String password){
        Connection connection = null; // Establishing the connection declaration
        PreparedStatement preparedStatement = null; // Prepared statement declaration
        ResultSet resultSet = null; // declaration of result set for iteration
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db"); // db Connection
            preparedStatement = connection.prepareStatement("SELECT password, username FROM Student WHERE username = ?"); // Select statement for
            preparedStatement.setString(1, username); // setting username to the prepared statement
            resultSet = preparedStatement.executeQuery(); // execution of the prepared statement
            if (!resultSet.isBeforeFirst()){
                // checking if the user already exists or not
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect Credentials!");
                alert.show();
                // showing alert if the student already exists
            }else {
                while (resultSet.next()){
                    // iteration of the student login in the db.
                    String checkPassword = resultSet.getString("password");
                    String checkUsername = resultSet.getString("username");
                    // if the student username found in the db then check if the entered password is correct or not.
                    if (checkPassword.equals(hashCryptPassword(password))){ // checking hashed password.
                        loggedStudent.add(username);
                        // switching the scene to dashboard if the password is matched
                        changeScene(e,"loggedIn.fxml", "Welcome to Student Enrollment!", checkUsername);
                    } else if ((checkPassword.isEmpty())) {
                        // if the password empty throw an error alert
                       // System.err.println("Password empty");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Empty  Password!");
                        alert.show();
                    } else {
                        // if it did not match the show an alert.
                        System.err.println("Password did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Incorrect Credentials!");
                        alert.show();
                    }
                }
            }
        }catch (SQLException t){
            // Handing SQL exception
            t.printStackTrace();
        }finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed

            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
        }
    }

    public static void setStudentInfo(String username, Label usernameLabel, Label IdLabel, Label nameLabel){
        Connection connection = null; // Declaring the connection
        PreparedStatement preparedStatement = null; // declaring prepared statement
        ResultSet resultSet = null; // declaring result statement
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db"); // creating a connection to the db
            preparedStatement = connection.prepareStatement("SELECT * FROM Student WHERE username = ?"); // setting a query for the db
            preparedStatement.setString(1, username); // assignment of first check in db
            resultSet = preparedStatement.executeQuery();// executing the query
            if (!resultSet.isBeforeFirst()){
                // if student not found thow an error
               // System.err.println("Student Not Found in DB");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Student does not exist in System records");
                alert.show();
                // show alert if the student not found
            }else {
                // if found then set the label to necessary info using the while results.
                while (resultSet.next()){
                    String checkUsername = resultSet.getString("username");
                    String firstLastName = resultSet.getString("firstName") +" "+ resultSet.getString("lastName");
                    String ID = resultSet.getString("studentNumber");
                    usernameLabel.setText(checkUsername);
                    IdLabel.setText(ID);
                    nameLabel.setText(firstLastName);
                    //setting all the required info of the student upon called
                }
            }
        }catch (SQLException t){
            // Handing SQL exception
            t.printStackTrace();
        }finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
        }
    }
}
