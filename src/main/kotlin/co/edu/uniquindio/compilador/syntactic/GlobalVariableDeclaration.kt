package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una variable global
 * @author Francisco Alejandro Hoyos Rojas
 */
class GlobalVariableDeclaration(var typeVariable: Token, var dataType: Token, var identifier: Token, var endSentence: Token) {

    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Global Variable Declaration")
        parent.children.add(TreeItem("Data Type: ${dataType.lexeme}"))
        parent.children.add(TreeItem("Identifier: ${identifier.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "GlobalVariableDeclaration(typeVariable=$typeVariable, dataType=$dataType, identifier=$identifier, endSentence=$endSentence)"
    }


}