package co.edu.uniquindio.compilador.syntactic

import javafx.scene.control.TreeItem

/**
 * Clase que representa un argumento
 * @author Francisco Alejandro Hoyos Rojas
 */
class Argument (var expression: Expresion?) {
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Argument")
        parent.children.add(expression?.getTree())
        return parent
    }
    override fun toString(): String {
        return "Argument(expression=$expression)"
    }
}