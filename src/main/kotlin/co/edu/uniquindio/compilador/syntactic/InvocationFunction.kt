package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de invocación de una función
 * @author Francisco Alejandro Hoyos Rojas
 */
class InvocationFunction(
    var point: Token,
    var id: Token,
    var parenthesisLeft: Token,
    var arguments: ArrayList<Argument>,
    var parenthesisRight: Token,
    var endSentence: Token
) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Invocation Function")
        parent.children.add(TreeItem("Identifier:  ${id?.lexeme}"))
        var parentArgument = TreeItem("Arguments")
        for (argument in arguments) {
            parent.children.add(argument.getTree())
        }
        parent.children.add(parentArgument)
        return parent
    }
}