package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa la unidad de compilación
 * @author Francisco Alejandro Hoyos Rojas
 */
class CompilationUnit (var functions : ArrayList<Funcion>, var globalVariables : ArrayList<GlobalVariableDeclaration> ) {

    /**
     * Función encargada de obtener el árbol visual
     */
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Compilation Unit")
        var parentFunctions = TreeItem("Functions")
        for (funcion in functions) {
            parentFunctions.children.add(funcion.getTree())
        }
        var parentGlobalVariables = TreeItem("Global Variables")
        for (globalVariable in globalVariables){
            parentGlobalVariables.children.add(globalVariable.getTree())
        }
        parent.children.add(parentFunctions)
        parent.children.add(parentGlobalVariables)
        return parent
    }

    /**
     * Función encargada de agregar los símbolos  a la tabla de símbolos
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     */
    fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>) {
        for (function in functions) {
            var ambit = Symbol("CompilationUnit",null,false,null,0,0)
            function.addSymbols(symbolsTable, semanticErrors,ambit)
        }
    }

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     */
    fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>) {
        for (function in functions) {
            function.analyzeSemantic(symbolsTable, semanticErrors)
        }
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode(): String {
        var sourceCode = "import javax.swing.*;\n public class Principal{\n"
        for (globalVariable in globalVariables){
            sourceCode += globalVariable.getJavaCode()
        }
        for (function in functions) {
            sourceCode += function.getJavaCode()
        }
        sourceCode += "\n}"
        return sourceCode
    }
}