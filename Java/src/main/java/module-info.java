module assignment.assignment2_javafx {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;

    opens assignment.assignment2_javafx to javafx.fxml;
    exports assignment.assignment2_javafx;
}