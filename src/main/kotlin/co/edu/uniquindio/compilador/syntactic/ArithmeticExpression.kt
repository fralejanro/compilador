package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión aritmética
 * @author Francisco Alejandro Hoyos Rojas
 */
class ArithmeticExpression (): Expresion(){
    var arithmeticExpression: ArithmeticExpression? = null
    var arithmeticExpressionAux: ArithmeticExpression? = null
    var operator: Token? = null
    var numericalValue: NumericalValue? = null

    constructor(arithmeticExpression: ArithmeticExpression?, operator: Token?, arithmeticExpressionAux: ArithmeticExpression?) : this() {
        this.arithmeticExpression = arithmeticExpression
        this.operator = operator
        this.arithmeticExpressionAux = arithmeticExpressionAux
    }

    constructor(arithmeticExpression: ArithmeticExpression?) : this() {
        this.arithmeticExpression = arithmeticExpression
    }

    constructor(numericalValue: NumericalValue?, operator: Token?, arithmeticExpressionAux: ArithmeticExpression?) : this() {
        this.numericalValue = numericalValue
        this.operator = operator
        this.arithmeticExpressionAux = arithmeticExpressionAux
    }

    constructor(numericalValue: NumericalValue?) : this() {
        this.numericalValue = numericalValue
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("Arithmetic Expression")
    }
}