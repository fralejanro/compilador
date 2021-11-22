package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de retorno
 * @author Francisco Alejandro Hoyos Rojas
 */
class Return (var reservedWord: Token, var expression: Expresion?, var endSentence: Token) : Sentence() {


    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Return")
        parent.children.add(expression?.getTree())
        return parent
    }

    override fun toString(): String {
        return "Return(reservedWord=$reservedWord, expression=$expression, endSentence=$endSentence)"
    }

}
