package co.edu.uniquindio.compilador.semantic

import co.edu.uniquindio.compilador.syntactic.CompilationUnit

/**
 * Clase que representa el analizador semántico
 * @author Francisco Alejandro Hoyos Rojas
 */
class SemanticAnalyzer (var compilationUnit: CompilationUnit){
    var semanticErrors: ArrayList<SemanticError> = ArrayList()
    var symbolsTable: SymbolsTable = SymbolsTable(semanticErrors)

    /**
     * Función encargada de agregar los símbolos  a la tabla de símbolos
     */
    fun addSymbols() {
        compilationUnit.addSymbols(symbolsTable, semanticErrors)
    }

    /**
     * Función encargada de analizar la semántica
     */
    fun analyzeSemantic() {
        compilationUnit.analyzeSemantic(symbolsTable, semanticErrors)
    }
}