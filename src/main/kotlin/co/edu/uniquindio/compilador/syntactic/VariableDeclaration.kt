package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.ReservedWords
import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
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

    override fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        symbolsTable.addValueSymbol(identifier.lexeme, dataType.lexeme,ambit,identifier.row,identifier.column)
    }

    override fun getJavaCode(): String {
        if(ReservedWords.valueOf(type.lexeme)== ReservedWords.INMUTABLE){
            return "final " +dataType.getJavaCode()+" "+identifier.lexeme +";"
        }else{
            return dataType.getJavaCode()+" "+identifier.lexeme +";"
        }
    }
}