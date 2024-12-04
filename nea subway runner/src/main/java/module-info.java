module com.example.neasubwayrunner {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.neasubwayrunner to javafx.fxml;
    exports com.example.neasubwayrunner;
}