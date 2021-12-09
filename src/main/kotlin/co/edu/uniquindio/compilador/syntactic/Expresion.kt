package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión
 * @author Francisco Alejandro Hoyos Rojas
 */
open abstract class Expresion() {

    /**
     * Función encargada de obtener el árbol visual
     */
    open fun getTree(): TreeItem<String>? {
        return null
    }

    /**
     * Función encarga de obtener el tipo de expresión
     * @param symbolsTable tabla de símbolos
     * @param ambit ambito de la expresión
     */
    open fun getType(symbolsTable: SymbolsTable, ambit: Symbol): String {
        return ""
    }

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     */
    open fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {}

    /**
     * Función encargada de obtener el código java
     */
    open fun getJavaCode(): String {
        return ""
    }

}