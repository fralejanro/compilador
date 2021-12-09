package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
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

    override fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        var ambitIf = Symbol(
            reservedWordIf!!.lexeme,
            null,
            ArrayList<String>(),
            reservedWordIf!!.row,
            reservedWordIf!!.column
        )

        for(sentence in sentencesIf){
            sentence.addSymbols(symbolsTable,semanticErrors,ambitIf)
        }

        var ambitThen = Symbol(
            reservedWordThen!!.lexeme,
            null,
            ArrayList<String>(),
            reservedWordThen!!.row,
            reservedWordThen!!.column
        )

        for(sentence in sentencesThen) {
            sentence.addSymbols(symbolsTable, semanticErrors, ambitThen)
        }
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(logicalExpression!=null){
            logicalExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }else{
            semanticErrors.add(
                SemanticError(
                    "Se encontro un error en al expresión lógica del ambito ${ambit.name}",
                    reservedWordIf!!.row,
                    reservedWordIf!!.column
                )
            )
        }

        var ambitIf = Symbol(
            reservedWordIf!!.lexeme,
            null,
            ArrayList<String>(),
            reservedWordIf!!.row,
            reservedWordIf!!.column
        )
        for (sentence in sentencesIf){
            sentence.analyzeSemantic(symbolsTable, semanticErrors, ambitIf)
        }

        var ambitThen = Symbol(
            reservedWordThen!!.lexeme,
            null,
            ArrayList<String>(),
            reservedWordThen!!.row,
            reservedWordThen!!.column
        )
        for (sentence in sentencesThen){
            sentence.analyzeSemantic(symbolsTable, semanticErrors, ambitThen)
        }
    }

    override fun getJavaCode(): String {
        var sourceCode = "if("+logicalExpression?.getJavaCode()+"){"
        for(sentence in sentencesIf){
            sourceCode += sentence.getJavaCode()
        }
        sourceCode += "}else{"
        for(sentence in sentencesThen){
            sourceCode += sentence.getJavaCode()
        }
        sourceCode += "}"
        return sourceCode
    }
}