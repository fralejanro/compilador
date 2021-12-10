package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.ReservedWords
import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una variable global
 * @author Francisco Alejandro Hoyos Rojas
 */
class GlobalVariableDeclaration(var typeVariable: Token, var dataType: Token, var identifier: Token, var endSentence: Token) {

    /**
     * Función encargada de obtener el árbol visual
     */
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Global Variable Declaration")
        parent.children.add(TreeItem("Data Type: ${dataType.lexeme}"))
        parent.children.add(TreeItem("Identifier: ${identifier.lexeme}"))
        return parent
    }

    /**
     * Función encargada de agregar los símbolos  a la tabla de símbolos
     * @param symbolsTable tabla de símbolos
     * @param ambit ambito de la sentencia
     */
    fun addSymbols(symbolsTable: SymbolsTable, ambit: Symbol) {
        symbolsTable.addValueSymbol(identifier.lexeme, dataType.lexeme,ambit,identifier.row,identifier.column)
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode(): String {
        return "public static final "+dataType.getJavaCode()+" "+identifier.lexeme +";"
    }

}