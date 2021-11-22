package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de decremento
 * @author Francisco Alejandro Hoyos Rojas
 */
class Decrement  (var identifier: Token, var operator: Token, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Increase")
        parent.children.add(TreeItem("Identifier:  ${identifier.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "Increase(identifier=$identifier, operator=$operator, endSentence=$endSentence)"
    }
}