package assignment.assignment2_javafx;

// Importing necessary JavaFx packages
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is a Main class where the application starts
 * This main class has the method to create all the necessary database tables for the project.
 * Once the tables are created the method is created to add necessary data to the tables through files.
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // The start method is the starting point of JavaFX applications.
        stage.setOnCloseRequest(event -> {
            // Setting an alert handler for the close request event of the stage.
            event.consume();
            // Taking the event to prevent it from going further.
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Confirm Exit");
            alert.setContentText("Are you sure you want to exit?");
            // Creating an alert to confirm the close.
            ButtonType exitButton = new ButtonType("Exit");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(exitButton, cancelButton);
            // Adding exit and cancel buttons to the alert dialog.

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == exitButton) {
                    stage.close();
                    // Close if the student selects the exit button.
                }
            });
        });
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        // Load the sample.fxml file and create the root node of the scene graph.
        stage.setTitle("Student Login!");
        // Setting the title of the stage.
        Scene scene = new Scene(root, 600, 600);
        // Creating a scene with the root node and dimensions.
        scene.getStylesheets().add("/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2_JavaFx/src/main/resources/assignment/assignment2_javafx/style.css");
        // Adding a CSS stylesheet
        stage.setScene(scene);
        // Setting the scene to the stage.
        stage.resizableProperty();
        // Setting the resizable property of the stage.
        stage.show();
        // Displaying the stage & scene.
    }


    public static void main(String[] args) {
        String studentTable = "Student"; // Defining the name of the student table.
        String url = "jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db"; // Defining the URL for connecting to the SQLite database.
        String arg1 = "CREATE TABLE IF NOT EXISTS "+studentTable+
        " (username Varchar(255), password Varchar(255), firstName Varchar(255), lastName Varchar(255)," +
                "studentNumber Varchar(255) UNIQUE,PRIMARY KEY ('username'))"; // Defining the SQL statement for creating the student table.
        String arg2 = "SELECT * from " + studentTable; // Defining the SQL statement for selecting all records from the student table.
        String col1 = "username"; // declaring username column
        String col2 = "firstName"; // declaring first name column
        String courseTable = "Course"; // declaring course table
        String courseArg1 = "CREATE TABLE IF NOT EXISTS "+courseTable+
                "(courseId int NOT NULL, courseName Varchar(255), capacity int, year Varchar(255), deliveryMode Varchar(255), " +
                "dayOfLecture Varchar(255), startTimeOfLecture TIME, endTimeOfLecture TIME, PRIMARY KEY ('courseId'))"; // Defining the SQL statement for creating the course table.
        String courseArg2 = "SELECT * FROM "+ courseTable; //declaring select statement for course table.
        String courseCol1 = "courseId"; // declaring course Id column
        String courseCol2 = "courseName"; // declaring course name column

        String insertCourseValuesStatement =  "INSERT OR IGNORE INTO "+courseTable+" (courseId, courseName, capacity, year, deliveryMode, dayOfLecture, startTimeOfLecture," +
                " endTimeOfLecture) VALUES (?,?,?,?,?,?,?,?)"; // Insert statement declaration for inserting course data

        String enrollTable = "enroll"; // Declaring enrol table
        String enrollArg = "CREATE TABLE IF NOT EXISTS "+enrollTable+ " (username Varchar(255), courseName Varchar(255))"; // declaring create statement for enroll table
        String enrollArg1 = "SELECT * FROM "+enrollTable; // declaring select statement for enrol table

        createDatabaseStructure(url, studentTable, arg1, arg2, col1, col2); // calling create the database structure for the student table method.
        createDatabaseStructure(url, courseTable,courseArg1,courseArg2,courseCol1,courseCol2); // calling create the database structure for the course table method.
        createDatabaseStructure(url, enrollTable, enrollArg, enrollArg1, col1, courseCol2); // calling create the database structure for the enroll table method.
        insertValues("/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2_JavaFx/src/course.csv", url, insertCourseValuesStatement); // calling insert values into the course table from a CSV file method.
        launch(args); // Launching the Java FX application.
    }

    public static void createDatabaseStructure(String url, String tableName,String arg1, String arg2,String col1, String col2){
       try{
           Connection connection = DriverManager.getConnection(url); // Establishing connection to the db using URL.
           Statement statement = connection.createStatement(); // Creating a statement object for executing SQL.
           int createTableCheck = statement.executeUpdate(arg1); // Executing the SQL query for creating the table and check the result.
           ResultSet resultSet = statement.executeQuery(arg2);   // Executing  the SQL for getting records from the table and get the result set.
           // System.out.println("Hurry !! "+ tableName + " Table successfully created/retrieved from Database");
           while (resultSet.next()){
               // iterating values from the result set.
               String a = resultSet.getString(col1);
               String b = resultSet.getString(col2);
               //System.out.println(a+" | "+b);
           }
           statement.close();  // Closing the statement.
           connection.close(); // Closing the db connection.
       }catch (SQLException t){
           // Handling SQLException that would occur using print stack trace.
           t.printStackTrace();
       }

    }

    public static void insertValues(String file, String url, String insertCourseValuesStatement){
        String line = "";
        int lineCount = 0;
        int count = 0;
        {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                // Creating a reader to read the file.
                while ((line = reader.readLine()) != null) {  // Read each line from the file.
                    if (lineCount == 0) {
                        lineCount++;
                        continue;
                    }
                    // Skip the first line (header) of the file.
                    count++;  // Increment the count to generate the courseId.
                    String[] values = line.split(","); // splitting the line into an array.
                    int courseId = count; // Generate the courseId based on the count.

                    // Retrieve values from the line.
                    String courseName = values[0];
                    String capacity = values[1];
                    String year = values[2];
                    String deliveryMode = values[3];
                    String day = values[4];
                    String lectureStartTime = values[5];
                    String lectureEndTime = (values[6]);

                    // Perform calculations to get the end time of the lecture.
                    SimpleDateFormat df = new SimpleDateFormat("H:mm");
                    Date d = df.parse(lectureStartTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, (int) (Double.parseDouble(lectureEndTime) * 60));
                    String lectureNewEndTime = df.format(cal.getTime());
                    //System.out.println( courseId+ " " +courseName +" "+ capacity+" "+ day +" "+ deliveryMode+ " "+ lectureStartTime +" "+lectureNewEndTime);
                    try{
                        Connection connection = DriverManager.getConnection(url);  // Establishing a connection to the db using the URL.
                        PreparedStatement statement = connection.prepareStatement(insertCourseValuesStatement); // Preparing the statement for inserting values into the table.
                        statement.setString(1, String.valueOf(courseId));
                        statement.setString(2, courseName);
                        statement.setString(3, capacity);
                        statement.setString(4, year);
                        statement.setString(5, deliveryMode);
                        statement.setString(6, day);
                        statement.setString(7, lectureStartTime);
                        statement.setString(8, lectureNewEndTime);
                        // Setting the parameter values.

                        statement.executeUpdate();  // Executing the update statement to insert values into the table.

                        statement.close(); // Closing the statement
                        connection.close(); //Closing the connection
                    }catch (SQLException t){
                        // Handling SQLException
                        t.printStackTrace();
                    }
                }
            }catch (NullPointerException e) {
                // Handling a NullPointerException if the file is null.
                System.err.println("File is null.");
            } catch (FileNotFoundException e) {
                // Handling a FileNotFoundException if the file is not found.
                System.err.println("File not found.");
            }catch (IOException e) {
                // Handling an IOException if an error occurs while reading the file.
                System.err.println("IO Exception");
            } catch (ParseException e) {
                // Throwing a RuntimeException if an error occurs while parsing the lecture start time.
                throw new RuntimeException(e);
            }
        }
        //System.out.println("Insert Successful");
    }




}
