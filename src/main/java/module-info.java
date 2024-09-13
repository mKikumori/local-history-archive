module com.example.local_history_archive {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.local_history_archive to javafx.fxml;
    exports com.example.local_history_archive;
}