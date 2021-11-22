package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de un ciclo
 * @author Francisco Alejandro Hoyos Rojas
 */
class Loop (var reservedWord: Token, var parenthesisLeft:Token, var logicalExpression:LogicalExpression?, var parenthesisRight:Token, var keyLeft:Token, var sentences:ArrayList<Sentence>, var keyRight:Token):Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Loop")

        var parentSentences = TreeItem("Sentences")
        for (sentence in sentences) {
            parentSentences.children.add(sentence.getTree())
        }
        parent.children.add(parentSentences)
        return parent
    }
}