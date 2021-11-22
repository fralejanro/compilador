module co.edu.uniquindio.compilador {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens co.edu.uniquindio.compilador.app to javafx.fxml;
    opens co.edu.uniquindio.compilador.controllers to javafx.fxml;
    opens co.edu.uniquindio.compilador.lexical to javafx.fxml;
    opens co.edu.uniquindio.compilador.syntactic to javafx.fxml;
    exports co.edu.uniquindio.compilador.app;
    exports co.edu.uniquindio.compilador.controllers;
    exports co.edu.uniquindio.compilador.lexical;
    exports co.edu.uniquindio.compilador.syntactic;
}


