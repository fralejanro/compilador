package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem
/**
 * Clase que representa una función
 * @author Francisco Alejandro Hoyos Rojas
 */
class Funcion (
    var wordFunction: Token?,
    var returnType: Token,
    var identifier : Token,
    var parenthesisLeft: Token?,
    var parameters : ArrayList<Parameter>,
    var parenthesisRight : Token?,
    var keyLeft : Token?,
    var sentences : ArrayList<Sentence>,
    var keyRight : Token?
){
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Funcion")
        parent.children.add(TreeItem("Identifier:  ${identifier.lexeme}"))
        parent.children.add(TreeItem("Return:  ${returnType.lexeme}"))

        var parentParameter = TreeItem("Parameters")
        for (parameter in parameters) {
            parentParameter.children.add(parameter.getTree())
        }

        var parentSentence = TreeItem("Sentences")
        for (sentence in sentences) {
            parentSentence.children.add(sentence.getTree())
        }
        parent.children.add(parentParameter)
        parent.children.add(parentSentence)
        return parent
    }

    override fun toString(): String {
        return "Funcion(wordFunction=$wordFunction, returnType=$returnType, identifier=$identifier, parenthesisLeft=$parenthesisLeft, parameters=$parameters, parenthesisRight=$parenthesisRight, keyLeft=$keyLeft, sentences=$sentences, keyRight=$keyRight)"
    }
}