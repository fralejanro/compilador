package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Category
import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
import javafx.scene.control.TreeItem

/**
 * Clase que representa una expresión de cadena
 * @author Francisco Alejandro Hoyos Rojas
 */
class StringExpression(): Expresion() {
    var string: Token? = null
    var plus: Token? = null
    var expression: Expresion? = null

    constructor(string: Token?, plus: Token?, expression: Expresion?) : this() {
        this.string = string
        this.plus = plus
        this.expression = expression
    }

    constructor(string: Token) : this() {
        this.string = string
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("String Expression")
    }

    override fun getType(symbolsTable: SymbolsTable, ambit: Symbol): String {
        return "STRING";
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(expression!= null){
            expression!!.analyzeSemantic(symbolsTable, semanticErrors, ambit)
        }
    }

    override fun getJavaCode(): String {
        var sourceCode = string?.lexeme
        if(expression!=null){
            var sourceCodeExpression = expression?.getJavaCode()
            if(expression is ArithmeticExpression){
                var tem = expression as ArithmeticExpression;
                if(tem.numericalValue?.type?.category == Category.IDENTIFICADOR){
                    return sourceCode +"+"+ expression?.getJavaCode()
                }
            }
            sourceCodeExpression = sourceCodeExpression?.substring(0, sourceCodeExpression.length - 1)
            return sourceCode+"+"+sourceCodeExpression+"\""
        }
        return ""+sourceCode+""
    }

}