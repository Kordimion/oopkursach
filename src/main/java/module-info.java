module leti.oop.coursework.oopkursach {
    requires javafx.controls;
    requires javafx.fxml;


    opens leti.oop.coursework to javafx.fxml;
    exports leti.oop.coursework;
}