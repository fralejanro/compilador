package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de una declaración de variable
 * @author Francisco Alejandro Hoyos Rojas
 */
class VariableDeclaration (var type: Token, var dataType: Token, var identifier: Token, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Variable Declaration")
        parent.children.add(TreeItem("Type : ${type.lexeme}"))
        parent.children.add(TreeItem("Data Type: ${dataType.lexeme}"))
        parent.children.add(TreeItem("Identifier: ${identifier.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "VariableDeclaration(type=$type, dataType=$dataType, identifier=$identifier, endSentence=$endSentence)"
    }

}