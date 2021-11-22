package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión de cadena
 * @author Francisco Alejandro Hoyos Rojas
 */
class StringExpression(): Expresion() {
    var string: Token? = null
    var plus: Token? = null
    var expression: Expresion? = null

    constructor(string: Token?, plus: Token?, expression: Expresion?) : this() {
        this.string = string
        this.plus = plus
        this.expression = expression
    }

    constructor(string: Token) : this() {
        this.string = string
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("String Expression")
    }

}