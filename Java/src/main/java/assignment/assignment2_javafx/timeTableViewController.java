package assignment.assignment2_javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This is a timetable view controller where the student can view the timetable.
 * The methods are created to change the dates according to the semester dates weekly.
 * The myTimeTableView method will display the course in the sophisticated manner.
 * The switch cases are used to retrieve the location of the day and time in the grid pane.
 */

public class timeTableViewController implements Initializable {

    @FXML
    private Button backButton; // button for back

    @FXML
    private GridPane timeTableGridPane; // Grid pane for timetable

    @FXML
    private Button forwardButton; // button for next week

    @FXML
    private Button previoudButton; // button for previous week


    String dbUrl = "jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db"; // declaring the url
    String getUsername = SqlConnection.loggedStudent.get(0); // declaring the logged in student
    boolean forwardButtonClicked = false; // setting the button activation of week traverse
    LocalDate startDate = LocalDate.of(2023,3,1); // declaring the initial date
    LocalDate endDate = LocalDate.of(2023, 5,31); // declaring the final date of sem
    LocalDate currentDate = startDate; // setting current date to start date.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // clearing the first row for week dates
        clearFirstRow(timeTableGridPane);
        // calling the method to display the date
        displayDates();
        // calling the method for timetable view.
        myTimeTableView(dbUrl, getUsername);

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            //back button for switching the scene to main dashboard
            @Override
            public void handle(ActionEvent event) {
                SqlConnection.changeScene(event, "loggedIn.fxml", "Welcome to DashBoard", getUsername);
            }
        });

        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            // forward button for changing the week dates
            @Override
            public void handle(ActionEvent event) {
                clearFirstRow(timeTableGridPane);
                forwardButtonClicked = true;
                // activation of the previous button only if the forward button is clicked
                if (currentDate.plusWeeks(1).isBefore(endDate)) {
                    // adding a week to the initial dates if clicked
                    currentDate = currentDate.plusWeeks(1);
                    displayDates();
                    // calling method for displaying the dates
                }
            }
        });

        previoudButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // activated the button only if the forward button is clicked
                if (forwardButtonClicked){
                    if (currentDate.minusWeeks(1).isAfter(startDate)) {
                        clearFirstRow(timeTableGridPane);
                        // subtracting a week to the initial dates if clicked
                        currentDate = currentDate.minusWeeks(1);
                        displayDates();
                        // calling method for displaying the dates
                    }
                }
            }
        });

    }


    // Displaying the dates for weekly updates
    private void displayDates() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(" dd MMM yy");
        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        int column = 1;
        // using the local dates and setting the dates
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            Label dateLabel = new Label(date.format(dateFormatter));
            timeTableGridPane.add(dateLabel, column, 0);
            column++;
        }
    }

    // method clearing the first row
    private void clearFirstRow(GridPane gridPane) {
        // creating a observable list
        ObservableList<Node> children = gridPane.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : children) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex != null && rowIndex == 0) {
                nodesToRemove.add(node);
            }
            // removing the data from the grid first row
        }
        children.removeAll(nodesToRemove);
        // remove the nodes
    }



    // Creating Time table view method
    private void myTimeTableView(String url, String username){
        Connection connection = null; // Declaring the connection for db
        PreparedStatement psGetStudent = null; // Declaring the get student prepared statement
        PreparedStatement psGetEnrollment = null; // Declaring the get enrollment prepared statement
        PreparedStatement psGetCourse = null;// Declaring the get course prepared statement
        ResultSet resultSet = null; // Declaring the result set
        ResultSet resultCourseDetails = null; // Declaring the result set for course
        try{
            connection = DriverManager.getConnection(url); // Establishing the connection.
            psGetStudent = connection.prepareStatement("SELECT * FROM student WHERE username = ?"); // SQL query to set username check
            psGetStudent.setString(1, username);
            psGetEnrollment = connection.prepareStatement("SELECT * FROM enroll WHERE username = ?"); // SQL query to check the course.
            psGetEnrollment.setString(1, username);
            resultSet = psGetEnrollment.executeQuery(); // get the enrollment of the student
            ObservableList<String> courses = FXCollections.observableArrayList();
            Color borderColor = Color.rgb(153, 170, 246);
            BorderStroke borderStroke = new BorderStroke(borderColor, BorderStrokeStyle.SOLID,null,BorderStroke.MEDIUM);
            // Creating a border using the border stroke
            Border border = new Border(borderStroke);
            // creating observable list to add the courses to the grid
            while (resultSet.next()){
                String getCourse = resultSet.getString("courseName");
                courses.add(getCourse);
                // adding the result set to the list
            }
            psGetCourse = connection.prepareStatement("Select * FROM course WHERE courseName = ?"); // SQL query to check the course.
            for (String course : courses) {
                psGetCourse.setString(1, course);
                resultCourseDetails = psGetCourse.executeQuery();
                String courseName = resultCourseDetails.getString("courseName").replace(" ", "\n"); // setting the course to the particular grid
                String fromTime = String.valueOf(resultCourseDetails.getString("startTimeOfLecture"));
                String toTime = String.valueOf(resultCourseDetails.getString("endTimeOfLecture"));
                String day = resultCourseDetails.getString("dayOfLecture");
                int column = getDayIndex(day);
                int startRow = getTimeIndex(fromTime);
                int endRow = getTimeIndex(toTime);
                Label lectureLabel = new Label();
                lectureLabel.setText(courseName);
                lectureLabel.setStyle("-fx-background-color: #8efccf");
                lectureLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                lectureLabel.setBorder(border);
                GridPane.setFillHeight(lectureLabel, true);
                GridPane.setFillWidth(lectureLabel, true);
                timeTableGridPane.add(lectureLabel, column, startRow, 1, endRow - startRow + 1);
                //setting the course exactly to the location of the time and day

            }
        }catch (SQLException t){
            // Handing SQL exception
            t.printStackTrace();
        } finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed
            if (psGetStudent != null){
                try{
                    psGetStudent.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psGetEnrollment != null){
                try{
                    psGetEnrollment.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psGetCourse != null){
                try{
                    psGetCourse.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (resultCourseDetails != null){
                try{
                    resultCourseDetails.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (resultSet != null){
                try{
                    resultSet.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (connection != null){
                try{
                    connection.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
        }

    }

    // switch method to get the location of the time in the grid pane
    private int getTimeIndex(String time){
        return switch (time) {
            case "8:00"-> 2;
            case "8:30" -> 3;
            case "9:00" -> 4;
            case "9:30" -> 5;
            case "10:00" -> 6;
            case "10:30" -> 7;
            case "11:00" -> 8;
            case "11:30" -> 9;
            case "12:00" -> 10;
            case "12:30" -> 11;
            case "13:00" -> 12;
            case "13:30" -> 13;
            case "14:00" -> 14;
            case "14:30" -> 15;
            case "15:00" -> 16;
            case "15:30" -> 17;
            case "16:00" -> 18;
            case "16:30" -> 19;
            case "17:00" -> 20;
            case "17:30" -> 21;
            default -> -1;
        };
    }

    // switch method to get the location of the day in the grid pane
    private int getDayIndex(String day){
        return switch (day) {
            case "Monday" -> 1;
            case "Tuesday" -> 2;
            case "Wednesday" -> 3;
            case "Thursday" -> 4;
            case "Friday" -> 5;
            case "Saturday" -> 6;
            case "Sunday" -> 7;
            default -> -1;
        };
    }

}
