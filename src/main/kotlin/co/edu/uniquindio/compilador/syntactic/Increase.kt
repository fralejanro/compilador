package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de un incremento
 * @author Francisco Alejandro Hoyos Rojas
 */
class Increase (var identifier: Token, var operator: Token, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Increase")
        parent.children.add(TreeItem("Identifier:  ${identifier.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "Increase(identifier=$identifier, operator=$operator, endSentence=$endSentence)"
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        var identifierAmbit = symbolsTable.getValueSymbol(identifier.lexeme, ambit)
        if (identifierAmbit == null) {
            semanticErrors.add(
                SemanticError(
                    "La variable ${identifier.lexeme} no existe dentro del ambito ${ambit.name}",
                    identifier.row,
                    identifier.column
                )
            )
        }else{
            if(identifierAmbit.type!="INTEGER" && identifierAmbit.type!="DOUBLE" && identifierAmbit.type != "FLOAT"){
                semanticErrors.add(
                    SemanticError(
                        "No es posible aumentar la variable ${identifier.lexeme} ya que su tipo de dato no es numérico",
                        identifier.row,
                        identifier.column
                    )
                )
            }
        }
    }

    override fun getJavaCode(): String {
        return identifier.lexeme+"++;"
    }
}