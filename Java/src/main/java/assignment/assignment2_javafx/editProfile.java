
package assignment.assignment2_javafx;

// Importing necessary JavaFX and SQL packages
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.*;

/**
 * This class has methods that allow the student to edit their profile.
 * This method involves getting values from text field and updating them in DB.
 */
public class editProfile {
    public static void editStudentProfile(String username, TextField newFirstNameTextField, TextField newLastNameTextField, TextField newPasswordTextField){
        // Getting the values from text field to update in the DB
        String advFirstName = newFirstNameTextField.getText().trim();
        String advLastName = newLastNameTextField.getText().trim();
        String advPassword = newPasswordTextField.getText();
        // Initialize database connection variables
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet resultSet = null;
        try {
            // Establishing a connection to the DB
            connection = DriverManager.getConnection("jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db");
            // Preparing a statement to check if the student exists in the DB
            preparedStatement = connection.prepareStatement("SELECT username FROM Student WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            // Checking if the student exists in the db
            if (!resultSet.isBeforeFirst()){
                // Displaying an error message if the student is not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Student does not exist in System records");
                alert.show();
            }else {
                // Check if any of the fields are empty
                if (advPassword.isEmpty() || advFirstName.isEmpty() || advLastName.isEmpty()){
                    System.err.println("Fields cannot be empty!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Fields cannot be empty!");
                    alert.show();
                    return;
                }else {
                    //Updating statement to modify the student's profile
                    updateStatement = connection.prepareStatement("UPDATE Student SET firstName = ?, lastName = ?, password = ? WHERE username = ?");
                    updateStatement.setString(1, advFirstName);
                    updateStatement.setString(2, advLastName);
                    updateStatement.setString(3, SqlConnection.hashCryptPassword(advPassword));
                    updateStatement.setString(4, username);
                    // Executing the update statement
                    int rowsAffected = updateStatement.executeUpdate();
                    Alert alert;
                    if (rowsAffected > 0) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Update successful!");
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Update failed.");
                    }
                    alert.show();
                }
            }
        }catch (SQLException t){
            // Handling any SQL exceptions
            t.printStackTrace();
        }finally {
            // Closing result set, statements, and connection using try catch
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
            }if (updateStatement != null){
                try {
                    updateStatement.close();
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
