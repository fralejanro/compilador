package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de lectura
 * @author Francisco Alejandro Hoyos Rojas
 */
class Read (var reservedWord: Token, var identifier: Token?, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Read")
        parent.children.add(TreeItem("Identifier:  ${identifier?.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "Read(reservedWord=$reservedWord, identifier=$identifier, endSentence=$endSentence)"
    }
}