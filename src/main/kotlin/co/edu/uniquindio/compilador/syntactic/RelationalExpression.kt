package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
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

    override fun toString(): String {
        return "RelationalExpression(arithmeticExpression=$arithmeticExpression, operator=$operator, arithmeticExpressionAux=$arithmeticExpressionAux)"
    }

}