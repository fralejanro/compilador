package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
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

    override fun toString(): String {
        return "LogicalExpression(relationalExpression=$relationalExpression, logicalOperator=$logicalOperator, logicalExpression=$logicalExpression)"
    }
}