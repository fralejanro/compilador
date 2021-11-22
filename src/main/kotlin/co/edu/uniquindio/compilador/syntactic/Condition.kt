package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de una condición
 * @author Francisco Alejandro Hoyos Rojas
 */
class Condition(
    var reservedWordIf: Token?,
    var parenthesisLeft: Token?,
    var logicalExpression: LogicalExpression?,
    var parenthesisRight: Token?,
    var keyLeftIf: Token?,
    var sentencesIf: ArrayList<Sentence>,
    var keyRightIf: Token?,
    var reservedWordThen: Token?,
    var keyLeftThen: Token?,
    var sentencesThen: ArrayList<Sentence>,
    var keyRightThen: Token?
) : Sentence(){

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Condition")
        var expresion = TreeItem("Expresion")
        expresion.children.add(logicalExpression?.getTree())
        parent.children.add(expresion)
        if (sentencesIf != null) {
            var parenteSentencesIf = TreeItem("Sentences If")
            for (sentence in sentencesIf) {
                parenteSentencesIf.children.add(sentence.getTree())
            }
            parent.children.add(parenteSentencesIf)
        } else {
            var parenteSentencesIf = TreeItem("Sentences If")
            parenteSentencesIf.children.add(TreeItem("Sentences If: Sentences empty"))
            parent.children.add(parenteSentencesIf)
        }
        if (sentencesThen != null) {
            var parenteSentencesThen = TreeItem("Sentences Then")
            for (sentence in sentencesThen) {
                parenteSentencesThen.children.add(sentence.getTree())
            }
            parent.children.add(parenteSentencesThen)
        } else {
            var parenteSentencesThen = TreeItem("Sentences Then")
            parenteSentencesThen.children.add(TreeItem("Sentences Then: Sentences empty"))
            parent.children.add(parenteSentencesThen)
        }
        return parent
    }

    override fun toString(): String {
        return "Condition(reservedWordIf=$reservedWordIf, parenthesisLeft=$parenthesisLeft, logicalExpression=$logicalExpression, parenthesisRight=$parenthesisRight, keyLeftIf=$keyLeftIf, sentencesIf=$sentencesIf, keyRightIf=$keyRightIf, reservedWordThen=$reservedWordThen, keyLeftThen=$keyLeftThen, sentencesThen=$sentencesThen, keyRightThen=$keyRightThen)"
    }


}