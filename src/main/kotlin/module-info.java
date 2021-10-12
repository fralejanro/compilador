module co.edu.uniquindio.compilador.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens co.edu.uniquindio.compilador.app to javafx.fxml;
    exports co.edu.uniquindio.compilador.app;
}