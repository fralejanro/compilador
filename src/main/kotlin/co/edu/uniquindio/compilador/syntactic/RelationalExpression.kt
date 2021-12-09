package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión relacional
 * @author Francisco Alejandro Hoyos Rojas
 */
class RelationalExpression() : Expresion() {
    var arithmeticExpression: ArithmeticExpression?=null
    var operator: Token?=null
    var arithmeticExpressionAux: ArithmeticExpression?=null

    constructor(arithmeticExpression:ArithmeticExpression, operator:Token, arithmeticExpressionAux:ArithmeticExpression):this(){
        this.arithmeticExpression=arithmeticExpression
        this.operator=operator
        this.arithmeticExpressionAux=arithmeticExpressionAux
    }

    constructor(operador: Token?) : this(){
        this.operator=operador
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("Relational Expression")
    }

    override fun getType(symbolsTable: SymbolsTable, ambit: Symbol): String {
        return "RELATIONAL"
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(arithmeticExpression!=null && arithmeticExpressionAux!=null){
            arithmeticExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
            arithmeticExpressionAux!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
        }else if(arithmeticExpression!=null){
            arithmeticExpression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
        }
    }

    override fun getJavaCode(): String {
        if(arithmeticExpression!=null && arithmeticExpressionAux!=null){
           return arithmeticExpression?.getJavaCode() + operator?.lexeme + arithmeticExpressionAux?.getJavaCode()
        }
        return arithmeticExpression?.getJavaCode()+""
    }
}