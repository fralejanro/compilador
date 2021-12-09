package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de lectura
 * @author Francisco Alejandro Hoyos Rojas
 */
class Read (var reservedWord: Token, var dataType: Token, var identifier: Token, var expression: StringExpression?, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Read")
        parent.children.add(TreeItem("Type:  ${dataType.lexeme}"))
        parent.children.add(TreeItem("Identifier:  ${identifier.lexeme}"))
        return parent
    }

    override fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        symbolsTable.addValueSymbol(identifier.lexeme, dataType.lexeme,ambit,identifier.row,identifier.column)
    }

    override fun getJavaCode(): String {
        when(dataType.lexeme){
            "INTEGER" -> {return "int ${identifier.lexeme} = Integer.parseInt(JOptionPane.showInputDialog(null,${expression?.getJavaCode()}));"}
            "DOUBLE" -> {return "double ${identifier.lexeme} = Double.parseDouble(JOptionPane.showInputDialog(null,${expression?.getJavaCode()}));"}
            "BOOLEAN" -> {return "boolean ${identifier.lexeme} = Boolean.parseBoolean(JOptionPane.showInputDialog(null,${expression?.getJavaCode()}));"}
        }
        return "String ${identifier.lexeme} = JOptionPane.showInputDialog(null,${expression?.getJavaCode()});"
    }

}