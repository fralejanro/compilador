package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa un argumento
 * @author Francisco Alejandro Hoyos Rojas
 */
class Argument (var expression: Expresion?) {

    /**
     * Función encargada de obtener el árbol visual
     */
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Argument")
        parent.children.add(expression?.getTree())
        return parent
    }

    /**
     * Función encargada de obtener el tipo del argumento
     */
    fun getType(symbolsTable: SymbolsTable, ambit: Symbol): String{
        return expression!!.getType(symbolsTable,ambit)
    }

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     * @param ambit ambito del argumento
     */
    fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        return expression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode(): String{
        return expression?.getJavaCode()+""
    }
}