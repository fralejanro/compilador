package co.edu.uniquindio.compilador.syntactic

import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión
 * @author Francisco Alejandro Hoyos Rojas
 */
open abstract class Expresion() {
    open fun getTree(): TreeItem<String>? {
        return null
    }
}