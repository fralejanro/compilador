package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem
/**
 * Clase que representa una función
 * @author Francisco Alejandro Hoyos Rojas
 */
class Funcion (
    var wordFunction: Token?,
    var returnType: Token,
    var identifier : Token,
    var parenthesisLeft: Token?,
    var parameters : ArrayList<Parameter>,
    var parenthesisRight : Token?,
    var keyLeft : Token?,
    var sentences : ArrayList<Sentence>,
    var keyRight : Token?
){
    /**
     * Función encargada de obtener el árbol visual
     */
    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Funcion")
        parent.children.add(TreeItem("Identifier:  ${identifier.lexeme}"))
        parent.children.add(TreeItem("Return:  ${returnType.lexeme}"))

        var parentParameter = TreeItem("Parameters")
        for (parameter in parameters) {
            parentParameter.children.add(parameter.getTree())
        }

        var parentSentence = TreeItem("Sentences")
        for (sentence in sentences) {
            parentSentence.children.add(sentence.getTree())
        }
        parent.children.add(parentParameter)
        parent.children.add(parentSentence)
        return parent
    }

    /**
     * Función encargada de obtener los tipos de parámetros
     */
    fun getTypeParameters(): ArrayList<String> {
        var typeParameters = ArrayList<String>()
        for (parameter in parameters) {
            typeParameters.add(parameter.dataType.lexeme)
        }
        return typeParameters
    }

    /**
     * Función encargada de agregar los símbolos  a la tabla de símbolos
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     * @param ambit ambito de la función
     */
    fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        symbolsTable.addSymbolFunction(
            identifier.lexeme,
            returnType.lexeme,
            getTypeParameters(),
            ambit,
            identifier.row,
            identifier.column
        )
        for (parameter in parameters) {
            var ambitFunction = Symbol(
                identifier.lexeme,
                returnType.lexeme,
                getTypeParameters(),
                identifier.row,
                identifier.column
            )
            symbolsTable.addValueSymbol(
                parameter.name.lexeme,
                parameter.dataType.lexeme,
                ambitFunction,
                parameter.name.row,
                parameter.name.column
            )
        }
        for (sentence in sentences) {
            var ambitFunction = Symbol(
                identifier.lexeme,
                returnType.lexeme,
                getTypeParameters(),
                identifier.row,
                identifier.column
            )
            sentence.addSymbols(symbolsTable, semanticErrors, ambitFunction)
        }
    }

    /**
     * Función encargada de analizar la semántica
     * @param symbolsTable tabla de símbolos
     * @param semanticErrors errores semánticos
     */
    fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>) {
        for (sentence in sentences) {
            var ambit = Symbol(
                identifier.lexeme,
                returnType.lexeme,
                getTypeParameters(),
                identifier.row,
                identifier.column
            )
            sentence.analyzeSemantic(symbolsTable, semanticErrors, ambit)
        }
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode(): String {
        var sourceCode = ""
        if (identifier.lexeme == "main") {
            sourceCode = "public static void main(String[] args){"
        } else {
            sourceCode = "public static " + returnType.getJavaCode() + " " + identifier.lexeme
            if(parameters.size == 0) {
                sourceCode+="( ){"
            }else {
                sourceCode += "("
                for (parameter in parameters) {
                    sourceCode += parameter.getJavaCode() + ","
                }
                sourceCode = sourceCode.substring(0, sourceCode.length - 1)
                sourceCode += "){"
            }
        }
        for (sentence in sentences) {
            sourceCode += sentence.getJavaCode()
        }
        sourceCode += "}"
        return sourceCode
    }
}