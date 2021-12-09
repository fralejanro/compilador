package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión lógica
 * @author Francisco Alejandro Hoyos Rojas
 */
class LogicalExpression(): Expresion() {
    var relationalExpression: RelationalExpression? = null
    var logicalOperator: Token? = null
    var logicalExpression: LogicalExpression? = null

    constructor(relationalExpression: RelationalExpression?, logicalOperator: Token?, logicalExpression: LogicalExpression?) : this() {
        this.relationalExpression = relationalExpression
        this.logicalOperator = logicalOperator
        this.logicalExpression = logicalExpression
    }

    constructor(logicalValue: RelationalExpression?) : this() {
        this.relationalExpression = logicalValue
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("Logical Expression")
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(relationalExpression!=null && logicalExpression ==null){
            relationalExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }else if(relationalExpression!=null && logicalExpression!=null){
            relationalExpression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
            logicalExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }else if(logicalExpression!=null){
            logicalExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }
    }

    override fun getJavaCode(): String {
        if(relationalExpression!=null&& logicalExpression!=null){
            return relationalExpression?.getJavaCode() + logicalOperator?.lexeme + logicalExpression?.getJavaCode()
        }else if(logicalExpression!=null){
            return logicalExpression?.getJavaCode()+""
        }else if( relationalExpression!=null && relationalExpression?.arithmeticExpression==null && relationalExpression?.arithmeticExpressionAux==null){
            return relationalExpression?.operator?.getJavaCode() ?: ""
        }
        return relationalExpression?.getJavaCode()+""
    }

}