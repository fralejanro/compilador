package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de repetición
 * @author Francisco Alejandro Hoyos Rojas
 */
class Repeat (
    var reservedWord: Token,
    var parenthesisLeft:Token,
    var keyLeft:Token,
    var sentences:ArrayList<Sentence>,
    var keyRight: Token,
    var separator: Token,
    var repetitions: Token,
    var parenthesisRight:Token,
    var endSentence: Token
    ):Sentence()
{

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Repeat")
        var parentSentences = TreeItem("Sentences")
        if(sentences.size >0){
            for (sentence in sentences) {
                parentSentences.children.add(sentence.getTree())
            }
        }else{
            parentSentences.children.add(TreeItem("Sentences: Sentences empty"))
        }
        parent.children.add(parentSentences)
        parent.children.add(TreeItem("Repetitions: ${repetitions.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "Repeat(reservedWord=$reservedWord, parenthesisLeft=$parenthesisLeft, keyLeft=$keyLeft, sentences=$sentences, keyRight=$keyRight, separator=$separator, repetitions=$repetitions, parenthesisRight=$parenthesisRight, endSentence=$endSentence)"
    }
}