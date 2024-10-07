package assignment.assignment2_javafx;

// Importing necessary JavaFx packages
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.awt.Desktop;

/**
 * This controller class has all the necessary attributes like images, text fields, list views and buttons that allow the student to enroll and withdraw.
 * This class has methods that allow the students to search a course, enrol or withdraw according to their desire.
 * This class also helps the students to export the enrollment
 */

public class enrollController implements Initializable {

    @FXML
    private Label IdLabel;  // Label for displaying student ID

    @FXML
    private Button backButton;  // Button for going back

    @FXML
    private Button logOutButton;  // Button for logging out

    @FXML
    private ImageView loggedImage4;  // ImageView for student image

    @FXML
    private ImageView arrowImage;  // ImageView for an image

    @FXML
    private Label nameLabel;  // Label for displaying student name

    @FXML
    private TextField searchBarTextField;  // TextField for searchbar

    @FXML
    private TextField searchEnrollTextField;  // TextField for enroll search bar

    @FXML
    private Button searchButton;  // Button for search

    @FXML
    private Button addButton;  // Button for adding student enrolment

    @FXML
    private Label studentIDLabel;  // Label for displaying student ID

    @FXML
    public ListView<String> courseListView;  // ListView for displaying course list

    @FXML
    public ListView<String> courseDetailListView;  // ListView for displaying course details

    @FXML
    public ListView<String> courseEnrolledListView;  // ListView for displaying enrolled courses

    @FXML
    private Button searchEnrollButton;  // Button for searching

    @FXML
    private Button removeButton;  // Button for removing student enrolment

    @FXML
    private Button exportButton;  // Button for exporting


    /**
     * The below-mentioned methods:
     * Initializes the student information.
     * Loads the courses from the database into a list view
     * A new list view on the ring side will display the course information ex: capacity, year and time.
     * The methods mention below will allow the student to enrol, withdraw and export any course depending on the availability of the course.
     * These below variables are re-used in different methode multiple times.
     */

    String dbUrl = "jdbc:sqlite:/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2.db"; // The URL for connecting to the SQLite database file located at the specified path
    String courseNameCol = "courseName"; // The name of the column in the database table that stores the course names
    String capacityCol = "capacity"; // The name of the column in the database table that stores the capacity of a course
    String enrollTable = "enroll"; // The name of the table in the database that stores enrollment information
    String getUsername = SqlConnection.loggedStudent.get(0); // Retrieve the username of the logged-in student from the SqlConnection class and assign it to the getUsername variable
    String exportUrl = "/Users/vishalmahanteshkodagali/IdeaProjects/Assignment2_JavaFx/src/main/resources/assignment/assignment2_javafx/exports"; // The path where exported files will be stored



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setting the student information (ID, student ID, and name) using the logged-in student's username
        SqlConnection.setStudentInfo(getUsername, IdLabel, studentIDLabel, nameLabel);

        // Loading the initial list of available courses from the database and populate the courseListView
        initialLoadCourses(dbUrl,courseNameCol,courseListView);

        // Displaying the list of courses in which the student is currently enrolled, retrieved from the database
        displayEnrolledCourses(dbUrl,courseNameCol,courseEnrolledListView,getUsername,enrollTable);

        // Selecting the first course in the courseListView by default
        courseListView.getSelectionModel().selectFirst();

        // Selecting the first enrolled course in the courseEnrolledListView by default
        courseEnrolledListView.getSelectionModel().selectFirst();

        // Fetching and display the details of the selected course in the courseListView
        fetchDetailsOfCourse(dbUrl,courseListView.getSelectionModel().getSelectedItem(),courseListView,courseNameCol);

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            //Creating a button event for logging out the student
            @Override
            public void handle(ActionEvent event) {
                //creating a new alert to get student confirmation for log out.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Logout");
                alert.setContentText("Are you sure you want to logout?");
                // creating buttons for alert.
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);
                // Check the student's choice and handle accordingly
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yesButton) {
                        // Clear the logged-in student arraylist information and switch the scene to the login screen
                        SqlConnection.loggedStudent.clear();
                        SqlConnection.changeScene(event, "sample.fxml", "Student login!", null);
                    }
                });
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            // Creating a button event for back function.
            @Override
            public void handle(ActionEvent event) {
                //switch the scene to the dashboard screen
                SqlConnection.changeScene(event, "loggedIn.fxml", "Welcome to Student Enrollment!", SqlConnection.loggedStudent.get(0));
            }
        });

        courseListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Handle the mouse click event on the courseListView
                String selectedItem = courseListView.getSelectionModel().getSelectedItem();
                // Get the selected item from the courseListView
                if (selectedItem != null) {
                    // If an item is selected (not null), proceed
                    fetchDetailsOfCourse(dbUrl,courseListView.getSelectionModel().getSelectedItem(),courseListView,courseNameCol);
                    // Fetching the details of the selected course using the fetchDetailsOfCourse method
                    // Passing the database URL (dbUrl), the selected course name, and the courseListView itself as arguments
                }
            }
        });

        // Setting an action event handler for the searchButton
        // When clicked, call the searchBar method passing the database URL (dbUrl), course name column (courseNameCol),
        // the text from searchBarTextField, the courseListView, the string "course", and the logged-in username
        searchButton.setOnAction(event -> searchBar(dbUrl, courseNameCol, searchBarTextField.getText(), courseListView, "course", getUsername));

        // Setting an action event handler for the searchEnrollButton
        // When clicked, call the searchBar method passing the database URL (dbUrl), course name column (courseNameCol),
        // the text from searchEnrollTextField, the courseEnrolledListView, the string "enroll", and the logged-in username
        searchEnrollButton.setOnAction(event -> searchBar(dbUrl, courseNameCol,searchEnrollTextField.getText(), courseEnrolledListView, "enroll", getUsername));

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Set an action event handler for the addButton
                // Insert the selected items into the database using the insertSelectedItems method
                // Pass the database URL (dbUrl), the logged-in username, the selected item from courseListView,
                // and the capacity column (capacityCol)
                insertSelectedItems(dbUrl,getUsername,courseListView.getSelectionModel().getSelectedItem(),capacityCol);

                // Displaying the enrolled courses in the courseEnrolledListView using the displayEnrolledCourses method
                // Passing the database URL (dbUrl), course name column (courseNameCol),
                // the courseEnrolledListView, and the logged-in username
                displayEnrolledCourses(dbUrl,courseNameCol,courseEnrolledListView,getUsername,enrollTable);

                // Fetching the details of the selected course using the fetchDetailsOfCourse method
                // Passing the database URL (dbUrl), the selected course name, and the courseListView
                fetchDetailsOfCourse(dbUrl,courseListView.getSelectionModel().getSelectedItem(),courseListView,courseNameCol);
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Setting an action event handler for the removeButton
                // Creating an alert dialog for confirmation
                // Setting the title, header text, and content text
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Withdraw Confirmation!");
                alert.setHeaderText("Withdraw Course");
                alert.setContentText("Are you sure you want to withdraw this Course?");
                // Creating two button types for "Yes" and "No"
                // Setting the button types of the alert dialog
                ButtonType yesButton = new ButtonType("Yes");
                ButtonType noButton = new ButtonType("No");
                alert.getButtonTypes().setAll(yesButton, noButton);
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yesButton) {
                        withdrawCourses(dbUrl,capacityCol,getUsername, courseEnrolledListView.getSelectionModel().getSelectedItem());
                        // Withdraw the course using the withdrawCourses method
                        // Pass the database URL (dbUrl), the capacity column (capacityCol),
                        // the logged-in username, and the selected item from courseEnrolledListView

                        displayEnrolledCourses(dbUrl,courseNameCol,courseEnrolledListView,getUsername,enrollTable);
                        // Display the enrolled courses in the courseEnrolledListView using the displayEnrolledCourses method
                        // Pass the database URL (dbUrl), course name column (courseNameCol),
                        // the courseEnrolledListView, and the logged-in username


                        fetchDetailsOfCourse(dbUrl,courseListView.getSelectionModel().getSelectedItem(),courseListView,courseNameCol);
                        // Fetch the details of the selected course using the fetchDetailsOfCourse method
                        // Pass the database URL (dbUrl), the selected course name, and the courseListView

                    }
                });
            }
        });


        // Create a tooltip with the message "Click on export to download your Enrollment!"
        // and assign it to the exportTip variable
        Tooltip exportTip = new Tooltip("Click on export to download your Enrollment!");
        exportButton.setTooltip(exportTip);
        // Set the tooltip for the exportButton to the exportTip tooltip

        exportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exportStudentEnrolment(dbUrl,exportUrl,getUsername);
                // Calling the exportStudentEnrolment method with the provided arguments to export the student's enrollment

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Export successful!");
                alert.setHeaderText(SqlConnection.loggedStudent.get(0)+".txt file has been exported!");
                alert.setContentText("Click on Yes to open the File");
                // Creating two button types for "Yes" and "No"
                // Setting the button types of the alert dialog
                ButtonType yes = new ButtonType("Yes");
                ButtonType No = new ButtonType("No");
                alert.getButtonTypes().setAll(yes, No);
                alert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType == yes) {
                        openFile(exportUrl+"/"+SqlConnection.loggedStudent.get(0)+".txt");
                        // If the user clicks on "Yes" in the alert, call the openFile method
                        // to open the exported file with the specified file path

                    }
                });

            }
        });
    }

    private void initialLoadCourses(String url, String courseNameCol, ListView view) {
        Connection connection = null; // Initialize connection to the database using the provided URL
        PreparedStatement psInitialStatement = null;  // Initialize Prepare SQL statement to select courseName and capacity from the Course table
        ResultSet resultSet = null; // Initialize Executable result set

        try{
            connection = DriverManager.getConnection(url);

            // Preparing a statement to retrieve course names and capacities from the Course table
            psInitialStatement = connection.prepareStatement("SELECT courseName, capacity FROM Course");

            // Executing the query and obtain the result set
            resultSet = psInitialStatement.executeQuery();

            // Creating an observable list to store the initial results
            ObservableList<String> initialResults = FXCollections.observableArrayList(); // Create an observable list to store the initial results

            // Iterate over the result set
            while (resultSet.next()){
                String courseName = resultSet.getString(courseNameCol);  // Adding the courseName to the initialResults list
                initialResults.add(courseName);  // Retrieving the course name from the current row and add the course name to the initialResults list
            }
            view.setItems(initialResults); // Setting the items of the view with the initialResults list

        }catch (SQLException t){
            // Handling any SQL exceptions that occur
            t.printStackTrace();
        }finally {
            // Closing the result set, prepared statement, and connection in a
            // finally to ensure they are always closed
            if (resultSet != null){
                try {
                    resultSet.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (psInitialStatement != null){
                try{
                    psInitialStatement.close();
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

    private void displayEnrolledCourses(String url, String courseNameCol, ListView enrolledCourseListView, String username, String enrollTable ) {
        Connection connection = null; // Initialize a database connection
        PreparedStatement psEnrolledCourses = null;  // Initialize a Prepare statement to retrieve enrolled courses for the specified username
        ResultSet resultSet = null; //Initialize Executable query to obtain the result set

        try{
            connection = DriverManager.getConnection(url); // Establishing a database connection
            psEnrolledCourses = connection.prepareStatement("SELECT courseName FROM "+enrollTable+" WHERE username = ?");
            psEnrolledCourses.setString(1, username);
            // Executing the query and obtain the result set
            resultSet = psEnrolledCourses.executeQuery();
            ObservableList<String> initialResults = FXCollections.observableArrayList(); // Creating an observable list to store the enrolled courses

            // Iterate over the result set
            while (resultSet.next()){
                // Retrieving the course name from the current row
                String courseName = resultSet.getString(courseNameCol);
                initialResults.add(courseName);  // Adding the course name to the initialResults list
            }
            enrolledCourseListView.setItems(initialResults);   // Setting the items of the enrolledCourseListView with the initialResults list
        }catch (SQLException t){
            // Handling any SQL exceptions that occur
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
            if (psEnrolledCourses != null){
                try{
                    psEnrolledCourses.close();
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
    private void fetchDetailsOfCourse(String url, String courseName, ListView courseListView, String courseNameCol) {
        Connection connection = null; // Initialize a database connection
        PreparedStatement psEnrolledCourses = null; // Initialize a prepared statement to retrieve course details
        ResultSet resultSet = null; // Initialize a result set to store the query results

        try{
            connection = DriverManager.getConnection(url); // Establish a database connection

            // Prepare a statement to retrieve course details for the specified course name
            psEnrolledCourses = connection.prepareStatement("SELECT * FROM course WHERE courseName = ?");
            psEnrolledCourses.setString(1, courseName);

            // Execute the query and obtain the result set
            resultSet = psEnrolledCourses.executeQuery();

            // Create an observable list to store the course details
            ObservableList<String> courseDetailResult = FXCollections.observableArrayList();

            // Iterate over the result set
            while (resultSet.next()){
                String course = resultSet.getString(courseNameCol);
                String capacity = resultSet.getString("capacity");
                String year = resultSet.getString("year");
                String mode = resultSet.getString("deliveryMode");
                String day = resultSet.getString("dayOfLecture");
                String time = resultSet.getString("startTimeOfLecture");
                String toTime = resultSet.getString("endTimeOfLecture");
                courseDetailResult.add("Course Name: "+course);

                // Check if the capacity is "N/A" and add the appropriate capacity information
                if (capacity.equals("N/A")){
                    courseDetailResult.add("Capacity: No Limit");
                }else {
                    courseDetailResult.add("Capacity: "+capacity);
                }
                courseDetailResult.add("Year: "+year);
                courseDetailResult.add("Mode: "+mode);
                courseDetailResult.add("Day: "+day);
                courseDetailResult.add("Time: "+time+"-"+toTime);
            }
            courseDetailListView.setItems(courseDetailResult);   // Set the items of the courseListView with the courseDetailResult list
        }catch (SQLException t){
            // Handle any SQL exceptions that occur
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
            if (psEnrolledCourses != null){
                try{
                    psEnrolledCourses.close();
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

    private void searchBar(String url, String courseNameCol, String searchText, ListView listView, String tableName, String getUsername) {
       listView.getItems().clear(); // Clear the items in the listView

        Connection connection = null; // Initialize a database connection
        PreparedStatement psSearchBarStatement = null; // Initialize a prepared statement for searching in the course table
        PreparedStatement psSearchBarStatement1 = null; // Initialize a prepared statement for searching in the enroll table
        ResultSet resultSet = null; // Initialize a result set to store the query results

        try{
            if (tableName.equals("course")){
                connection = DriverManager.getConnection(url); // Establish a database connection
                psSearchBarStatement = connection.prepareStatement("SELECT * FROM "+tableName+" WHERE courseName LIKE ?");
                psSearchBarStatement.setString(1, "%"+searchText+"%");

                // Execute the query and obtain the result set
                resultSet = psSearchBarStatement.executeQuery();
                ObservableList<String> searchResult = FXCollections.observableArrayList();
                searchResult.clear();

                // Iterate over the result set
                while (resultSet.next()){
                    String courseName = resultSet.getString(courseNameCol);
                    searchResult.add(courseName); // Add the courseName to the searchResult list
                }
                listView.setItems(searchResult);  // Set the items of the listView with the searchResult list
            }else if (tableName.equals("enroll")){
                connection = DriverManager.getConnection(url); // Establish a database connection
                psSearchBarStatement1 = connection.prepareStatement("SELECT * FROM "+tableName+" WHERE username = ? AND courseName LIKE ?");
                psSearchBarStatement1.setString(1, getUsername);
                psSearchBarStatement1.setString(2, "%"+searchText+"%");
                resultSet = psSearchBarStatement1.executeQuery(); // Execute the query and obtain the result set
                ObservableList<String> searchResult = FXCollections.observableArrayList(); // Create an observable list to store the search results
                searchResult.clear();

                // Iterate over the result set
                while (resultSet.next()){
                    String courseName = resultSet.getString(courseNameCol);
                    searchResult.add(courseName); // Add the courseName to the searchResult list
                }
                listView.setItems(searchResult); // Set the items of the listView with the searchResult list
            }
        }catch (SQLException t){
            // Handle any SQL exceptions that occur
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
            }if (psSearchBarStatement != null){
                try{
                    psSearchBarStatement.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psSearchBarStatement1 != null){
                try{
                    psSearchBarStatement1.close();
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

    private void insertSelectedItems(String url,String username, String courseName, String capacityCol){
        Connection connection = null; // Initialize a database connection
        PreparedStatement psInsert = null; // Initialize a prepared statement for inserting in the enrol table
        PreparedStatement psUpdate = null; // Initialize a prepared statement for updating capacity in the course table
        PreparedStatement checkCapacity = null; // Initialize a prepared statement for checking capacity in the course table
        PreparedStatement psCourseEnrolled = null; // Initialize a prepared statement for checking in the enrol table
        ResultSet resultSet = null; // Initialize a result set to store enrol query results
        ResultSet checkCourse = null; // Initialize a result set to store course query results

        try{
            connection = DriverManager.getConnection(url);

            // Prepare a statement to check if the user is already enrolled in the course
            psCourseEnrolled = connection.prepareStatement("SELECT username, courseName FROM enroll WHERE username = ? and courseName = ?");
            psCourseEnrolled.setString(1, SqlConnection.loggedStudent.get(0));
            psCourseEnrolled.setString(2, courseName);
            checkCourse = psCourseEnrolled.executeQuery();

            if (checkCourse.isBeforeFirst()){
                System.err.println("Enrollment Already exits!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You have already enrolled in this course.");
                alert.show();
            }else {
                checkCapacity = connection.prepareStatement("SELECT capacity, courseName FROM course WHERE courseName = ?");
                checkCapacity.setString(1, courseName);
                resultSet = checkCapacity.executeQuery();
                String getCapacity = resultSet.getString(capacityCol);
                //System.out.println(getCapacity);
                if (getCapacity.equals("N/A")){
                    psInsert = connection.prepareStatement("INSERT INTO enroll (username, courseName) VALUES (?,?)");
                    psInsert.setString(1, username);
                    psInsert.setString(2, courseName);
                    psInsert.executeUpdate();
                }else {
                    int capacityCheck = Integer.parseInt(getCapacity);
                    if (capacityCheck > 0){
                        psInsert = connection.prepareStatement("INSERT INTO enroll (username, courseName) VALUES (?,?)");
                        psInsert.setString(1, username);
                        psInsert.setString(2, courseName);
                        psInsert.executeUpdate();

                        psUpdate = connection.prepareStatement("UPDATE course SET capacity = capacity - ? WHERE courseName = ?");
                        psUpdate.setString(1, String.valueOf(1));
                        psUpdate.setString(2, courseName);
                        psUpdate.executeUpdate();
                    }else {
                       // System.err.println("Cannot Enroll into this course because there is no Seats left");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("No Seats Available!");
                        alert.show();
                    }
            }

        }

        }catch (SQLException t){
            t.printStackTrace();
        }finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psUpdate != null){
                try{
                    psUpdate.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (psCourseEnrolled != null) {
                try {
                    psCourseEnrolled.close();
                } catch (SQLException t) {
                    t.printStackTrace();
                }
            }
            if (checkCapacity != null){
                try{
                    checkCapacity.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (checkCourse != null){
                try{
                    checkCourse.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (resultSet != null){
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
    private void withdrawCourses(String url, String capacityCol, String username, String courseName){
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement checkCapacity = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(url);

            checkCapacity = connection.prepareStatement("SELECT capacity, courseName FROM course WHERE courseName = ?");
            checkCapacity.setString(1, courseName);
            resultSet = checkCapacity.executeQuery();
            String getCapacity = resultSet.getString(capacityCol);
            //System.out.println(getCapacity);
            if (getCapacity.equals("N/A")){
                psInsert = connection.prepareStatement("DELETE FROM enroll WHERE username = ? and courseName = ?");
                psInsert.setString(1, username);
                psInsert.setString(2, courseName);
                psInsert.executeUpdate();
            }else {
                int capacityCheck = Integer.parseInt(getCapacity);
                if (capacityCheck >= 0){
                    psInsert = connection.prepareStatement("DELETE FROM enroll WHERE username = ? and courseName = ?");
                    psInsert.setString(1, username);
                    psInsert.setString(2, courseName);
                    psInsert.executeUpdate();

                    psUpdate = connection.prepareStatement("UPDATE course SET capacity = capacity + ? WHERE courseName = ?");
                    psUpdate.setString(1, String.valueOf(1));
                    psUpdate.setString(2, courseName);
                    psUpdate.executeUpdate();
                }
            }
        }catch (SQLException t){
            t.printStackTrace();
        }finally {
            // Close the result set, prepared statement, and connection in a
            // finally-block to ensure they are always closed
            if (psInsert != null){
                try{
                    psInsert.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psUpdate != null){
                try{
                    psUpdate.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (checkCapacity != null){
                try{
                    checkCapacity.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (resultSet != null){
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

    private void exportStudentEnrolment(String url, String exportUrl, String username){
        Connection connection = null;
        PreparedStatement psExportStatement = null;
        PreparedStatement psGetStudentDetails = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;

        try{
            connection = DriverManager.getConnection(url);
            psExportStatement = connection.prepareStatement("SELECT courseName FROM enroll where username = ?");
            psExportStatement.setString(1, username);
            resultSet = psExportStatement.executeQuery();
            psGetStudentDetails = connection.prepareStatement("SELECT username, studentNumber, firstName, lastName FROM student WHERE username = ?");
            psGetStudentDetails.setString(1, username);
            resultSet1 = psGetStudentDetails.executeQuery();
            String ID = resultSet1.getString("studentNumber");
            String fName = resultSet1.getString("firstName");
            String lName = resultSet1.getString("lastName");
            FileWriter fileWriter = new FileWriter(exportUrl + "/" + username + ".txt");
            ResultSetMetaData metaData = resultSet.getMetaData();
            fileWriter.write("--------------------------------------------------------------\n");
            fileWriter.write("Username: "+username+"\n");
            fileWriter.write("--------------------------------------------------------------\n");
            fileWriter.write("Student ID: "+ID+"\tName: "+fName+" "+lName+"\n");
            fileWriter.write("--------------------------------------------------------------\n");
            fileWriter.write("Your Enrolment:\n");
            int colCount = metaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= colCount; i++) {
                    Object cellValue = resultSet.getObject(i);
                    fileWriter.write("\t* "+ String.valueOf(cellValue) + "\t");
                }
                fileWriter.write("\n");
            }
            fileWriter.write("--------------------------------------------------------------\n");
            fileWriter.close();

           // System.out.println("Please check the path. Data exported successfully.");

        }catch (SQLException | IOException t){
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
            }if (resultSet1 != null){
                try{
                    resultSet1.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }
            if (psGetStudentDetails != null){
                try{
                    psExportStatement.close();
                }catch (SQLException t){
                    t.printStackTrace();
                }
            }if (psExportStatement != null){
                try{
                    psExportStatement.close();
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

    private void openFile(String filename) {
        File file = new File(filename);// Creating a new File object with a file name of my interest.
        if (Desktop.isDesktopSupported()) { // Checking if the current desktop supports the class.
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) { // Checking if file exists.
                try {
                    desktop.open(file); // Opening the file using the default application with its file type.
                } catch (IOException e) { // Catching any IO-Exception that occur at the file opening process.
                    e.printStackTrace(); // Printing the stack trace of the exception.
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("File does not exist!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Desktop is not supported!");
            alert.show();
        }
    }

}
