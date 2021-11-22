package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa un valor numérico
 * @author Francisco Alejandro Hoyos Rojas
 */
class NumericalValue(var symbol: Token?, var  type:Token) {

    fun getTree(): TreeItem<String> {
        return TreeItem("Type: ${type.lexeme} : symbol: ${symbol?.lexeme} ")
    }

    override fun toString(): String {
        return "NumericalValue(symbol=$symbol, type=$type)"
    }
}