package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de arreglo
 * @author Francisco Alejandro Hoyos Rojas
 */
class Array(var dataType: Token, var identifier: Token, var arguments: ArrayList<Argument>) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Array")
        parent.children.add(TreeItem("Data type: ${dataType?.lexeme}"))
        parent.children.add(TreeItem("Identifier:  ${identifier?.lexeme}"))

        var parentArguments = TreeItem("Arguments")
        for (argument in arguments) {
            parentArguments.children.add(argument.getTree())
        }
        parent.children.add(parentArguments)
        return parent
    }

    override fun addSymbols(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        symbolsTable.addValueSymbol(
            identifier.lexeme,
            dataType.lexeme,
            ambit,
            identifier.row,
            identifier.column
        )
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        for (argument in arguments){
            argument!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
            var dataTypeArgument = argument.getType(symbolsTable,ambit)
            if(dataTypeArgument!=dataType.lexeme){
                semanticErrors.add(
                    SemanticError(
                        "El tipo de dato de la expresión no coincide con el tipo de dato del arreglo (${dataType.lexeme}) ",
                        identifier.row,
                        identifier.column
                    )
                )
            }
        }
    }

    override fun getJavaCode(): String {
        var sourceCode = dataType.getJavaCode() + " " + identifier.lexeme + "[] = {"
        for (argument in arguments){
            sourceCode += argument.getJavaCode() +","
        }
        sourceCode = sourceCode.substring(0,sourceCode.length-1)
        sourceCode += "};"
        return sourceCode
    }
}