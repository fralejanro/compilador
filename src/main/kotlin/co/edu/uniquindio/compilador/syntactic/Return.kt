package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentencia de retorno
 * @author Francisco Alejandro Hoyos Rojas
 */
class Return (var reservedWord: Token, var expression: Expresion?, var endSentence: Token) : Sentence() {

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Return")
        parent.children.add(expression?.getTree())
        return parent
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        var typeExpression:String? =null
        if(expression != null) {
            expression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
            typeExpression= expression!!.getType(symbolsTable, ambit)
        }else{
            semanticErrors.add(
                SemanticError(
                "La expresion de retorno del ambito ${ambit.name} contiene un error",
                reservedWord.row,
                reservedWord.column)
            )
        }

        if (typeExpression != ambit.type) {
            semanticErrors.add(
                SemanticError(
                    "El tipo de dato de retorno $typeExpression no coincide con el tipo de retorno del método ${ambit.name} que es de tipo ${ambit.type}",
                    reservedWord!!.row,
                    reservedWord!!.column
                )
            )
        }
    }

    override fun getJavaCode(): String {
        return reservedWord.getJavaCode()+" "+ expression?.getJavaCode()+";"
    }
}