package co.edu.uniquindio.compilador.syntactic

import javafx.scene.control.TreeItem

/**
 * Clase abstracta que representa una sentencia
 * @author Francisco Alejandro Hoyos Rojas
 */
open abstract class Sentence() {
    open fun getTree(): TreeItem<String> {
       return TreeItem("Sentence")
    }
}