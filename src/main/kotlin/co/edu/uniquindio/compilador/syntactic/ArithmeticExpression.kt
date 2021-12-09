package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Category
import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.semantic.SemanticError
import co.edu.uniquindio.compilador.semantic.Symbol
import co.edu.uniquindio.compilador.semantic.SymbolsTable
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
    var getValueArray : GetValueArray? = null

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

    constructor(getValueArray: GetValueArray?, operator: Token?, arithmeticExpressionAux: ArithmeticExpression?) : this() {
        this.getValueArray = getValueArray
        this.operator = operator
        this.arithmeticExpressionAux = arithmeticExpressionAux
    }

    constructor(numericalValue: NumericalValue?) : this() {
        this.numericalValue = numericalValue
    }

    constructor(getValueArray: GetValueArray?) : this() {
        this.getValueArray = getValueArray
    }

    override fun getTree(): TreeItem<String> {
        return TreeItem("Arithmetic Expression")
    }

    override fun getType(symbolsTable: SymbolsTable, ambit: Symbol): String {
        if(getValueArray!=null && arithmeticExpressionAux!=null){
            var symbol = symbolsTable.getValueSymbol(getValueArray!!.identifierArray.lexeme,ambit)
            var typeAux = arithmeticExpressionAux!!.getType(symbolsTable,ambit)
            if(symbol?.type!="INTEGER"&& symbol?.type!="DECIMAL"){
                return ""
            }
            if(typeAux == "DOUBLE" || symbol.type == "DOUBLE"){
                return "DOUBLE"
            }else{
                return "INTEGER"
            }
        }
        if(getValueArray!=null && arithmeticExpression!=null){
            var symbol = symbolsTable.getValueSymbol(getValueArray!!.identifierArray.lexeme,ambit)
            var type = arithmeticExpression!!.getType(symbolsTable,ambit)
            if(symbol?.type!="INTEGER"&& symbol?.type!="DECIMAL"){
                return ""
            }
            if(type == "DOUBLE" || symbol.type == "DOUBLE"){
                return "DOUBLE"
            }else{
                return "INTEGER"
            }
        }
        if (arithmeticExpression !=null && arithmeticExpressionAux !=null){
            var type = arithmeticExpression!!.getType(symbolsTable,ambit)
            var typeAux = arithmeticExpressionAux!!.getType(symbolsTable,ambit)
            if(type == "DECIMAL" || typeAux == "DECIMAL"){
                return "DOUBLE"
            }else{
                return "INTEGER"
            }
        }else if (arithmeticExpression != null) {
            return arithmeticExpression!!.getType(symbolsTable, ambit)
        }else if (numericalValue !=null && arithmeticExpression !=null){
            var typeNumericalValue = getTypeNumericalValue(numericalValue,ambit,symbolsTable)
            var typeArithmeticExpression = arithmeticExpression!!.getType(symbolsTable,ambit)
            if(typeNumericalValue == "DECIMAL" || typeArithmeticExpression == "DECIMAL"){
                return "DOUBLE"
            }else{
                return "INTEGER"
            }
        }else if (numericalValue!=null){
            return getTypeNumericalValue(numericalValue,ambit,symbolsTable)
        }
        return ""
    }

    fun getTypeNumericalValue(
        numericalValue: NumericalValue?,
        ambit: Symbol,
        symbolsTable: SymbolsTable
    ): String {
        if (numericalValue!!.type.category == Category.ENTERO) {
            return "INTEGER"
        } else if (numericalValue!!.type.category == Category.DECIMAL) {
            return "DOUBLE"
        } else {
            var symbol = symbolsTable.getValueSymbol(numericalValue!!.type.lexeme, ambit)
            if (symbol != null) {
                return symbol.type!!
            }
        }
        return ""
    }

    override fun analyzeSemantic(symbolsTable: SymbolsTable, semanticErrors: ArrayList<SemanticError>, ambit: Symbol) {
        if(numericalValue!=null){
            if(numericalValue!!.type.category == Category.IDENTIFICADOR){
                var symbol = symbolsTable.getValueSymbol(numericalValue!!.type.lexeme, ambit)
                if(symbol==null){
                    semanticErrors.add(
                        SemanticError(
                            "El valor ${numericalValue!!.type.lexeme} no existe dentro del ambito ${ambit.name}",
                            numericalValue!!.type.row,
                            numericalValue!!.type.column
                        )
                    )
                }
            }
        }
        if(arithmeticExpression!=null){
            arithmeticExpression!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }
        if(arithmeticExpressionAux!=null){
            arithmeticExpressionAux!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }
        if(getValueArray!=null){
            getValueArray!!.analyzeSemantic(symbolsTable,semanticErrors,ambit)
        }
    }

    override fun getJavaCode(): String {
        var sourceCode = ""
        if(getValueArray!=null&&arithmeticExpressionAux!=null ){
            return getValueArray?.getJavaCode()+operator?.lexeme+arithmeticExpressionAux?.getJavaCode()
        }else if(getValueArray!=null&&arithmeticExpression!=null ){
            return getValueArray?.getJavaCode()+operator?.lexeme+arithmeticExpression?.getJavaCode()
        }else if(arithmeticExpression!=null&& arithmeticExpressionAux!=null) {
            return arithmeticExpression?.getJavaCode() + operator?.lexeme + arithmeticExpressionAux?.getJavaCode()
        }else if(arithmeticExpressionAux!=null && numericalValue!=null){
            return numericalValue?.getJavaCode()+ operator?.lexeme +arithmeticExpressionAux?.getJavaCode()
        }else if(arithmeticExpression!=null && numericalValue!=null){
            return numericalValue?.getJavaCode() + operator?.lexeme + arithmeticExpression?.getJavaCode()
        }
        else if(arithmeticExpression!=null){
            return arithmeticExpression?.getJavaCode()+""
        }else if(numericalValue!=null){
            return numericalValue?.getJavaCode()+""
        }
        return sourceCode
    }
}