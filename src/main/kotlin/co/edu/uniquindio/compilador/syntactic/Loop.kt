package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
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


    override fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        for(sentence in sentences){
            sentence.addSymbols(symbolsTable,semanticErrors,Symbol(reservedWord.lexeme, null,  ArrayList<String>(),reservedWord.row, reservedWord.column))
        }
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(logicalExpression!=null){
            logicalExpression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
        }else{
            semanticErrors.add(
                SemanticError(
                    "Se encontro un error en al expresión lógica del ambito ${ambit.name}",
                    reservedWord.row,
                    reservedWord.column
                )
            )
        }
        for(sentence in sentences){
            sentence.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }
    }

    override fun getJavaCode(): String {
        var sourceCode = "while("+ logicalExpression!!.getJavaCode()+"){"
        for(sentence in sentences){
            sourceCode += sentence.getJavaCode()
        }
        sourceCode += "}"
        return sourceCode
    }
}