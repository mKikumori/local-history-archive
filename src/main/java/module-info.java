module com.example.local_history_archive {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.local_history_archive to javafx.fxml;
    exports com.example.local_history_archive;
    exports com.example.local_history_archive.controller;
    opens com.example.local_history_archive.controller to javafx.fxml;
    exports com.example.local_history_archive.model;
    opens com.example.local_history_archive.model to javafx.fxml;
}