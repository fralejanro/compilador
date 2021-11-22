package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de impresion
 * @author Francisco Alejandro Hoyos Rojas
 */
class Print (var reservedWord: Token?, var parenthesisLeft: Token?, var expression: Expresion?, var parenthesisRight: Token?, var endSentence: Token?) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Print")
        parent.children.add(expression?.getTree())
        return parent
    }

    override fun toString(): String {
        return "Print(reservedWord=$reservedWord, parenthesisLeft=$parenthesisLeft, expression=$expression, parenthesisRight=$parenthesisRight, endSentence=$endSentence)"
    }


}
