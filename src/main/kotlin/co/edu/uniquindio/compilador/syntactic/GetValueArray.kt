package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable

/**
 * Clase que representa el obtener el valor de una posición de un arreglo
 * @author Francisco Alejandro Hoyos Rojas
 */
class GetValueArray(var reservedWord: Token, var parenthesisLeft: Token, var identifierArray: Token, var separator: Token,  var identifierPosition: Token, var parenthesisRight: Token) {

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     * @param ambit ambito
     */
    fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        val identifierArraySymbol = symbolsTable.getValueSymbol(identifierArray.lexeme,ambit)
        if(identifierArraySymbol===null){
            semanticErrors.add(
                SemanticError(
                    "El identificador ${identifierArray.lexeme} no se encuentra en el ambito",
                    identifierArray.row,
                    identifierArray.column
                )
            )
        }
        val identifierPositionSymbol = symbolsTable.getValueSymbol(identifierPosition.lexeme,ambit)
        if(identifierPositionSymbol===null){
            semanticErrors.add(
                SemanticError(
                    "El identificador ${identifierPosition.lexeme} no se encuentra en el ambito",
                    identifierPosition.row,
                    identifierPosition.column
                )
            )
        }
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode(): String {
       return identifierArray.lexeme+"["+identifierPosition.lexeme+"]"
    }
}