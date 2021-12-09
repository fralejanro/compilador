package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase abstracta que representa una sentencia
 * @author Francisco Alejandro Hoyos Rojas
 */
open abstract class Sentence() {
    open fun getTree(): TreeItem<String> {
       return TreeItem("Sentence")
    }

    /**
     * Función encargada de agregar los símbolos  a la tabla de símbolos
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     * @param ambit ambito de la sentencia
     */
    open fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {}

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     * @param ambit ambito de la sentencia
     */
    open fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {}

    /**
     * Función encargada de obtener el código java
     */
    open fun getJavaCode():String{
        return ""
    }
}