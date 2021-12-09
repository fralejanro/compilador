package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
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

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        var symbol = symbolsTable.getValueSymbol(identifier!!.lexeme,ambit)
        if(symbol==null){
            semanticErrors.add(
                SemanticError(
                    "La variable ${identifier!!.lexeme} no existe dentro del ambito ${ambit.name}",
                    identifier!!.row,
                    identifier!!.column
                )
            )
        }else{
            var dataType = symbol.type
            if(expression!=null){
                expression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
                var typeExpression = expression!!.getType(symbolsTable,ambit)
                if(typeExpression!= dataType){
                    semanticErrors.add(
                        SemanticError(
                            "El tipo de dato de la expresión ${typeExpression} no coincide con el tipo de dato de la variable ${identifier!!.lexeme} que es del tipo ${dataType}",
                            identifier!!.row,
                            identifier!!.column
                        )
                    )
                }
            }
        }
    }

    override fun getJavaCode(): String {
        var sourceCode =""
        sourceCode += identifier?.lexeme
        if(expression!=null){
            sourceCode +="="+expression?.getJavaCode()+";"
            return sourceCode
        }
        return sourceCode+";"
    }

}