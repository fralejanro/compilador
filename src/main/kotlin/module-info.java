module co.edu.uniquindio.compilador {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens co.edu.uniquindio.compilador.app to javafx.fxml;
    opens co.edu.uniquindio.compilador.controllers to javafx.fxml;
    exports co.edu.uniquindio.compilador.app;
    exports co.edu.uniquindio.compilador.controllers;
}


