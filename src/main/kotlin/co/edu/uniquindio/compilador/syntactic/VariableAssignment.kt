package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa una sentenia de asignación de una variable
 * @author Francisco Alejandro Hoyos Rojas
 */
class VariableAssignment(): Sentence() {
    var identifier: Token? = null
    var assignmentOperator: Token? = null
    var expression: Expresion? = null
    var invocation: InvocationFunction? = null
    var endSentence: Token? = null

    constructor(identifier: Token?, assignmentOperator: Token?, expression: Expresion?, endSentence: Token?) : this() {
        this.identifier = identifier
        this.assignmentOperator = assignmentOperator
        this.expression = expression
        this.endSentence = endSentence
    }

    constructor(identifier: Token?, assignmentOperator: Token?, invocation: InvocationFunction?) : this() {
        this.identifier = identifier
        this.assignmentOperator = assignmentOperator
        this.invocation = invocation
    }

    override fun getTree(): TreeItem<String> {
        var parent = TreeItem("Variable Assignment")
        parent.children.add(TreeItem("Identifier: ${identifier?.lexeme}"))
        return parent
    }

    override fun toString(): String {
        return "VariableAssignment(identifier=$identifier, assignmentOperator=$assignmentOperator, expression=$expression, invocation=$invocation, endSentence=$endSentence)"
    }

}