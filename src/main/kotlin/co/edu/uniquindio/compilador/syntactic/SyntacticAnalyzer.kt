package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Category
import co.edu.uniquindio.compilador.lexical.LexicalError
import co.edu.uniquindio.compilador.lexical.ReservedWords
import co.edu.uniquindio.compilador.lexical.Token

class SyntacticAnalyzer(var tokens : ArrayList<Token>) {
    /**
     * Atributo que representa la posición actual dentro del código fuente
     */
    var currentPosition = 0

    /**
     * Atributo que representa el token actual
     */
    var currentToken = tokens[0]

    /**
     * Atributo que representa la lista de funciones
     */
    var functionsCompilationUnit = ArrayList<Funcion>()

    /**
     * Atributo que representa la lista de variables globales
     */
    var globalVariablesCompilationUnit = ArrayList<GlobalVariableDeclaration>()

    /**
     * Atributo que representa los errores sintacticos
     */
    var errors = ArrayList<SyntacticError>()

    /**
     * Atributo que representa los tipos de datos
     */
    var dataType = listOf(ReservedWords.INTEGER, ReservedWords.DOUBLE, ReservedWords.BOOLEAN, ReservedWords.FLOAT, ReservedWords.STRING)

    /**
     * Funcion encargada de obtener el siguiente token
     */
    fun nextToken() {
        currentPosition++
        if (currentPosition < tokens.size) {
            currentToken = tokens[currentPosition]
        } else {
            currentToken = Token("", Category.FIN_CODIGO, currentToken.row, currentToken.column)
        }
    }

    /**
     * Función encargada de hacer backtracking
     */
    fun backtracking(positionToken: Int) {
        currentPosition = positionToken
        currentToken = if (positionToken < tokens.size) {
            tokens[positionToken]
        } else {
            Token("", Category.ERROR, 0, 0)
        }
    }

    /**
     * Función encargada de verificar si una categoría es una unidad de compilación
     * <UnidadDeCompilacion> :: = <FuncionesVariablesGlobales>
     */
    fun isCompilationUnit(): CompilationUnit? {
        isFunctionsOrGlobalVariableDeclaration()
        return CompilationUnit(functionsCompilationUnit,globalVariablesCompilationUnit)
    }

    /**
     * Función encargada de verificar si una categoría es una función o una variable globale
     * <FuncionesVariablesGlobales> :: = <Funcion><VariableGlobal> [<FuncionesVariablesGlobales>]
     */
    fun isFunctionsOrGlobalVariableDeclaration() {
        do{
            var next = false
            var function = isFunction()
            if(function!=null){
                next = true
                functionsCompilationUnit.add(function)
            }else {
                var globalVariableDeclaration = isGloblaVariableDeclaration()
                if (globalVariableDeclaration != null) {
                    next = true
                    globalVariablesCompilationUnit.add(globalVariableDeclaration)
                }else{
                    next = false;
                    functionsCompilationUnit.clear()
                    globalVariablesCompilationUnit.clear()
                    errors.add(SyntacticError("No se puede definir si es una función o una variable global",  currentToken.row, currentToken.column))
                }
            }
        }while(next && currentToken !=null && currentToken.category!=Category.FIN_CODIGO)
    }

    /**
     * Función encargada de verificar si una categoría es una función
     * <Funcion> ::= FUN <TipoDatoFuncion> identificador ([<Paramertros>]){ [<Sentencias>] }
     */
    fun isFunction(): Funcion? {
        if (currentToken.category == Category.PALABRA_RESERVADA && currentToken.lexeme == "FUN") {
            var function = currentToken
            nextToken()
            if (isDataType()) {
            } else {
                errors.add(SyntacticError("No se encontró el tipo de retorno",  currentToken.row, currentToken.column))
            }
            var returnType = currentToken
            nextToken()
            if (currentToken.category == Category.IDENTIFICADOR) {
                var identifier = currentToken
                nextToken()
                if (currentToken.category == Category.PARENTESIS_IZQUIERDO) {
                } else {
                    errors.add(SyntacticError("No se encontró el paréntesis izquierdo",  currentToken.row, currentToken.column))
                }
                var parenthesisLeft = currentToken
                nextToken()
                var parameters: java.util.ArrayList<Parameter> = isParameters()
                if (currentToken.category == Category.PARENTESIS_DERECHO) {
                } else {
                    errors.add(SyntacticError("No se encontró el paréntesis derecho",  currentToken.row, currentToken.column))
                }
                var parenthesisRight = currentToken
                nextToken()
                if (currentToken.category == Category.LLAVE_IZQUIERDA) {
                } else {
                    errors.add(SyntacticError("No se encontró la llave izquierda",  currentToken.row, currentToken.column))
                }
                var keyLeft = currentToken
                nextToken()
                var sentences = isSentences()
                if (currentToken.category == Category.LLAVE_DERECHA) {
                } else {
                    errors.add(SyntacticError("No se encontró la llave derecha",  currentToken.row, currentToken.column))
                }
                var keyRight = currentToken
                nextToken()
                return Funcion(
                    function,
                    returnType,
                    identifier,
                    parenthesisLeft,
                    parameters,
                    parenthesisRight,
                    keyLeft,
                    sentences, keyRight
                )
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un tipo de dato de una función
     * <TipoDatoFuncion> :: = INTEGER | DOUBLE | BOOLEAN | FLOAT | STRING | VOID
     */
    fun isDataType(): Boolean {
        return (currentToken.category === Category.PALABRA_RESERVADA && dataType.contains(ReservedWords.valueOf(currentToken.lexeme))) || currentToken.lexeme == ReservedWords.VOID.name
    }

    /**
     * Función encargada de verificar si una categoría es una lista de parametros
     */
    fun isParameters(): ArrayList<Parameter> {
        var parameters = ArrayList<Parameter>()
        var param = isParameter()
        while (param != null) {
            parameters.add(param)
            if (currentToken.category == Category.SEPARADOR) {
                nextToken()
                param = isParameter()
            } else {
                param = null
            }
        }
        return parameters
    }

    /**
     * Función encargada de verificar si una categoría es un parametro
     * <Parametro>::= <TipoDato> identificador
     */
    fun isParameter(): Parameter? {
        if (isDataType()) {
            var dataType = currentToken
            nextToken()
            if (currentToken.category == Category.IDENTIFICADOR) {
                var identifier = currentToken
                nextToken()
                return Parameter(dataType, identifier)
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una lista de sentencias
     * <Sentencias> ::= <Sentencia> [<Sentencias>]
     */
    fun isSentences(): java.util.ArrayList<Sentence> {
        var sentences = java.util.ArrayList<Sentence>()
        var sentence = isSentence()
        while (sentence != null) {
            sentences.add(sentence)
            sentence = isSentence()
        }
        return sentences
    }

    /**
     * Función encargada de verificar si una categoría es una sentencia
     * <Sentencia> ::= <Condicion> | <DeclaracionVariable> | <Asignacion> | <Impresion> | <Retorno> | <Lectura> | <Ciclo> | <Invocacion> | <Decremento> | <Incremento>
     */
    fun isSentence(): Sentence? {
        var condition: Condition? = isCondition()
        if (condition != null) {
            return condition
        }
        var variableDeclaration: VariableDeclaration? = isVariableDeclaration()
        if (variableDeclaration != null) {
            return variableDeclaration
        }
        var variableAssignment: VariableAssignment? = isVariableAssignment()
        if (variableAssignment != null) {
            return variableAssignment
        }
        var print: Print? = isPrint()
        if (print != null) {
            return print
        }
        var isReturn: Return? = isReturn()
        if (isReturn != null) {
            return isReturn
        }

        var read: Read? = isRead()
        if (read != null) {
            return read
        }

        var loop: Loop? = isLoop()
        if (loop != null) {
            return loop
        }

        /*TODO
        var invocationFunction : InvocationFunction? = isInvocationFunction()
        if(invocationFunction!=null){
            return invocationFunction
        }*/

        var increase : Increase? = isIncrease()
        if(increase!=null){
            return increase
        }

        var decrement : Decrement? = isDecrement()
        if(decrement!=null){
            return decrement
        }

        var repeat : Repeat? = isRepeat()
        if(repeat!=null){
            return repeat
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una variable global
     * <VariableGlobal> :: = GLOBAL identificador <FinSentencia>
     */
    fun isGloblaVariableDeclaration(): GlobalVariableDeclaration?{
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) == ReservedWords.GLOBAL) {
            var typeVariable = currentToken
            nextToken()
            if (isDataType()) {
                var dataType = currentToken
                nextToken()
                if (currentToken.category == Category.IDENTIFICADOR) {
                    var identifier = currentToken
                    nextToken()
                    if (currentToken.category == Category.FIN_SENTENCIA) {
                        var endSentence = currentToken
                        nextToken()
                        return GlobalVariableDeclaration(typeVariable,dataType, identifier, endSentence)
                    } else {
                        errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                    }
                } else {
                    errors.add(SyntacticError("No se encontró el identificador",  currentToken.row, currentToken.column))
                }
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una condición
     * <Condicion> ::= IF (<ExpresionLogica>) { [<Sentencias>] } THEN { [<Sentencias>] }
     */
    fun isCondition(): Condition? {
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) == ReservedWords.IF) {
            var reservedWordIf = currentToken
            nextToken()
            if (currentToken.category != Category.PARENTESIS_IZQUIERDO) {
                errors.add(SyntacticError("No se encontró el paréntesis izquierdo",  currentToken.row, currentToken.column))
            }
            var parenthesisLeft = currentToken
            nextToken()
            var logicalExpression = isLogicalExpression()
            if (logicalExpression == null) {
                errors.add(SyntacticError("No se encontró la expresión",  currentToken.row, currentToken.column))
            }
            nextToken()
            if (currentToken.category != Category.PARENTESIS_DERECHO) {
                errors.add(SyntacticError("No se encontró el paréntesis derecho",  currentToken.row, currentToken.column))
            }
            var parenthesisRight = currentToken
            nextToken()
            if (currentToken.category != Category.LLAVE_IZQUIERDA) {
                errors.add(SyntacticError("No se encontró la llave izquierda",  currentToken.row, currentToken.column))
            }
            var keyLeftIf = currentToken
            nextToken()
            var sentencesIf = isSentences()
            if (currentToken.category != Category.LLAVE_DERECHA) {
                errors.add(SyntacticError("No se encontró la llave derecha",  currentToken.row, currentToken.column))
            }
            var keyRightIf = currentToken
            nextToken()
            if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) != ReservedWords.THEN) {
                errors.add(SyntacticError("No se encontró el flujo THEN",  currentToken.row, currentToken.column))
            }
            var reservedWordThen = currentToken
            nextToken()
            if (currentToken.category != Category.LLAVE_IZQUIERDA) {
                errors.add(SyntacticError("No se encontró la llave izquierda",  currentToken.row, currentToken.column))
            }
            var keyLefThen = currentToken
            nextToken()
            var sentencesThen = isSentences()
            if (currentToken.category != Category.LLAVE_DERECHA) {
                errors.add(SyntacticError("No se encontró la llave derecha",  currentToken.row, currentToken.column))
            }
            var keyRighThen = currentToken
            nextToken()
            return Condition(reservedWordIf,parenthesisLeft,logicalExpression,parenthesisRight,keyLeftIf,sentencesIf,keyRightIf,reservedWordThen,keyLefThen,sentencesThen,keyRighThen)
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una declaración de variable
     * <DeclaracionVariable> ::= <TipoVariable> <TipoDato> identificador <FinSentencia>
     */
    fun isVariableDeclaration(): VariableDeclaration?{
        if (currentToken.category == Category.PALABRA_RESERVADA &&
            (ReservedWords.valueOf(currentToken.lexeme)== ReservedWords.MUTABLE || ReservedWords.valueOf(currentToken.lexeme)== ReservedWords.INMUTABLE)){
            var type = currentToken
            nextToken()
            if (isDataType()) {
                var dataType = currentToken
                nextToken()
                if (currentToken.category == Category.IDENTIFICADOR) {
                    var identifier = currentToken
                    nextToken()
                    if (currentToken.category == Category.FIN_SENTENCIA) {
                        var endSentence = currentToken
                        nextToken()
                        return VariableDeclaration(type, dataType, identifier, endSentence)
                    } else {
                        errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                    }
                } else {
                    errors.add(SyntacticError("No se encontró el identificador",  currentToken.row, currentToken.column))
                }
            }else{
                errors.add(SyntacticError("No se encontró el tipo de dato",  currentToken.row, currentToken.column))
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una asignación de una variable
     * <AsignacionVariable> :: = identificador <OperadorAsignacion> <Expresion><
     */
    fun isVariableAssignment(): VariableAssignment?{
        var auxPosition = currentPosition
        if (currentToken.category == Category.IDENTIFICADOR) {
            var id = currentToken
            nextToken()
            if (currentToken.category == Category.ASIGNACION) {
                var assignmentOperator = currentToken
                nextToken()
                var expression: Expresion? = isExpression()
                if (expression != null) {
                    if (currentToken.category == Category.FIN_SENTENCIA) {
                        var endSentence = currentToken
                            nextToken()
                            return VariableAssignment(id, assignmentOperator, expression, endSentence)
                        } else {
                        errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                        }
                    } else {
                    errors.add(SyntacticError("No se encontró la expresión",  currentToken.row, currentToken.column))
                   }
                }
            else {
               backtracking(auxPosition);
            }
        }
        return null
    }

    //TODO
    /*
    fun isInvocationFunction(): InvocationFunction? {
        if (currentToken.category == Category.PUNTO) {
            var point = currentToken
            nextToken()
            if (currentToken.category == Category.IDENTIFICADOR) {
                var id = currentToken
                nextToken()
                if (currentToken.category == Category.PARENTESIS_IZQUIERDO) {
                    var parenthesisLeft = currentToken
                    nextToken()
                    var arguments: java.util.ArrayList<Argument>? = null
                    arguments = isArguments()
                    if (currentToken.category == Category.PARENTESIS_DERECHO) {
                        var parenthesisRight = currentToken
                        nextToken()
                        if (currentToken.category == Category.FIN_SENTENCIA) {
                            var endSentence = currentToken
                            nextToken()
                            return InvocationFunction(point, id, parenthesisLeft, arguments, parenthesisRight, endSentence)
                        } else {
                          //
                        }
                    } else {
                       //
                    }
                } else {
                   //
                }
            } else {
               //
            }
        }
        return null
    }*/

    /**
     * Función encargada de verificar si una categoría es una lista de argumentos
     * <Argumentos> argumento [<Argumentos>],
     */
    fun isArguments(): java.util.ArrayList<Argument> {
        var arguments = java.util.ArrayList<Argument>()
        var argument: Argument? = isArgument()
        while (argument != null) {
            arguments.add(argument)
            if (currentToken.category == Category.SEPARADOR) {
                nextToken()
                argument = isArgument()
            } else {
                argument = null
            }
        }
        return arguments
    }

    /**
     * Función encargada de verificar si una categoría es un argumento
     * <Argumento> ::= <expresion>
     */
    fun isArgument(): Argument? {
        var expression: Expresion? = isExpression()
        if (expression != null) {
            return Argument(expression)
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una expresión
     * <Expresion> :: = <ExpresionAritmetica> | <ExpresionRelacional> | <ExpresionCadena> | <ExpresionLogica>
     */
    fun isExpression(): Expresion? {
        var auxPosition = currentPosition
        var isArithmeticExpression: ArithmeticExpression? = isArithmeticExpression()

        if (currentToken.category != Category.OPERADOR_RELACIONAL) {
            if (isArithmeticExpression != null) {
                return isArithmeticExpression
            }
        } else {
            backtracking(auxPosition);
        }

        var isRelationalExpression: RelationalExpression? = isRelationalExpression()
        if (currentToken.category != Category.OPERADOR_LOGICO) {
            if (isRelationalExpression != null) {
                return isRelationalExpression
            }
        } else {
            backtracking(auxPosition);
        }

        var isStringExpression: StringExpression? = isStringExpression()
        if (isStringExpression != null) {
            return isStringExpression
        }

        var isLogicalExpression: LogicalExpression? = isLogicalExpression()
        if (isLogicalExpression != null) {
            return isLogicalExpression
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una expresión aritmetica
     * <ExpresionAritmetica> ::= ( <ExpresionAritmetica> ) <OperadorAritmetico> <ExpresionAritmetica> | ( <ExpresionAritmetica> ) | <ValorNumerico> <OperadorAritmetico> <ExpresionAritmetica> | <ValorNumerico>
     */
    fun isArithmeticExpression(): ArithmeticExpression? {
        if (currentToken.category == Category.PARENTESIS_IZQUIERDO) {
            nextToken()
            val arithmeticExpression: ArithmeticExpression? = isArithmeticExpression()
            if (arithmeticExpression != null) {
                if (currentToken.category == Category.PARENTESIS_DERECHO) {
                    nextToken()
                    if (currentToken.category == Category.OPERADOR_ARITMETICO) {
                        val operator = currentToken
                        nextToken()
                        val arithmeticExpressionAux = isArithmeticExpression()
                        if (arithmeticExpressionAux != null) {
                            return ArithmeticExpression(arithmeticExpression, operator, arithmeticExpressionAux)
                        }
                    } else {
                        return ArithmeticExpression(arithmeticExpression)
                    }
                }
            }
        }
        val numericalValue: NumericalValue? = isNumericalValue()
        if (numericalValue != null) {
            nextToken()
            if (currentToken.category == Category.OPERADOR_ARITMETICO) {
                val operator = currentToken
                nextToken()
                val arithmeticExpression = isArithmeticExpression()
                if (arithmeticExpression != null) {
                    return ArithmeticExpression(numericalValue, operator, arithmeticExpression)
                }
            } else {
                return ArithmeticExpression(numericalValue)
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un valor numérico
     * <ValorNumerico> :: = <Signo> <Entero> | <Signo> <Decimal> | <Entero> | <Decimal> | Identificador
     */
    fun isNumericalValue(): NumericalValue? {
        var symbol: Token? = null
        if (currentToken.category == Category.OPERADOR_ARITMETICO && (currentToken.lexeme == "+" || currentToken.lexeme == "-")) {
            symbol = currentToken
            nextToken()
        }
        if (currentToken.category == Category.ENTERO || currentToken.category == Category.DECIMAL || currentToken.category == Category.IDENTIFICADOR) {
            var type = currentToken
            return NumericalValue(symbol, type)
        } else {
            return null
        }
    }

    /**
     * Función encargada de verificar si una categoría es una expresión relacional
     * <ExpresionRelacional> :: = <ExpresionArimetica> <OperadorRelacional> <ExpresionAritmetica> | <OperadorBooleano>
     */
    fun isRelationalExpression(): RelationalExpression? {
        var operator: Token? = null
        val arithmeticExpression: ArithmeticExpression? = isArithmeticExpression()
        if (arithmeticExpression != null) {
            if (currentToken.category == Category.OPERADOR_RELACIONAL) {
                operator = currentToken
                nextToken()
                var arithmeticExpressionAux: ArithmeticExpression? = isArithmeticExpression()
                if (arithmeticExpressionAux != null) {
                    return RelationalExpression(arithmeticExpression, operator, arithmeticExpressionAux)
                } else {
                    errors.add(SyntacticError("No se encontró la expresión aritmetica",  currentToken.row, currentToken.column))
                }
            } else {
                errors.add(SyntacticError("No se encontró el operador relacional",  currentToken.row, currentToken.column))
            }
        } else {
            operator = currentToken
            if (operator.category == Category.PALABRA_RESERVADA && ( ReservedWords.valueOf(operator.lexeme) == ReservedWords.TRUE || ReservedWords.valueOf(operator.lexeme) == ReservedWords.FALSE)
            ) {
                return RelationalExpression(operator)
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una expresión de cadena
     * <ExpresionCadena> :: = <Cadena> | <Cadena> + <Expresion>
     */
    fun isStringExpression(): StringExpression? {
        if (currentToken.category == Category.CADENA) {
            var string = currentToken
            nextToken()
            if (currentToken.category != Category.OPERADOR_ARITMETICO && currentToken.lexeme != "+") {
                return StringExpression(string)
            } else {
                var plus = currentToken
                nextToken()
                var expression = isExpression()
                if (expression != null) {
                    return StringExpression(string, plus, expression)
                } else {
                    errors.add(SyntacticError("No se encontró la expresión",  currentToken.row, currentToken.column))
                }
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una expresión lógica
     * <ExpresionLogica> ::= <ExpresionRelacional> <OperadorLogico> <ExpresionLogica> | <ExpresionRelacional>
     */
    fun isLogicalExpression(): LogicalExpression? {
        val relationalExpression = isRelationalExpression()
        if (relationalExpression != null) {
            if (currentToken.category == Category.OPERADOR_LOGICO && (currentToken.lexeme == "&&" || currentToken.lexeme == "||")) {
                val operator = currentToken
                nextToken()
                val logicalExpression = isLogicalExpression()
                if (logicalExpression != null) {
                    return LogicalExpression(relationalExpression, operator, logicalExpression)
                }
            } else if (currentToken.category == Category.OPERADOR_LOGICO && (currentToken.lexeme == "!")) {
                val operator = currentToken
                nextToken()
                val logicalExpression = isLogicalExpression()
                if (logicalExpression != null) {
                    return LogicalExpression(relationalExpression, operator, logicalExpression)
                }
            } else {
                return LogicalExpression(relationalExpression)
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una impresion
     * <Impresion> :: = PRINT(<Expresion>)<FinSentencia>
     */
    fun isPrint(): Print? {
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) == ReservedWords.PRINT) {
            var reservedWord = currentToken
            nextToken()
            if (currentToken.category != Category.PARENTESIS_IZQUIERDO) {
                errors.add(SyntacticError("No se encontró el paréntesis izquierdo",  currentToken.row, currentToken.column))
            }
            var parenthesisLeft = currentToken
            nextToken()
            var expression = isExpression()
            if (currentToken.category != Category.PARENTESIS_DERECHO) {
                errors.add(SyntacticError("No se encontró el paréntesis derecho",  currentToken.row, currentToken.column))
            }
            var parenthesisRight = currentToken
            nextToken()
            if (currentToken.category != Category.FIN_SENTENCIA) {
                errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
            }
            var endSentence = currentToken
            nextToken()
            return Print(reservedWord,parenthesisLeft, expression, parenthesisRight, endSentence)
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un retorno
     * <Retorno> :: = RETURN <Expresion> <FinSentencia>
     */
    fun isReturn(): Return? {
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) == ReservedWords.RETURN) {
            var reservedWord = currentToken
            nextToken()
            var expression = isExpression()
            if (expression == null) {
                errors.add(SyntacticError("No se encontró la expresión de retorno",  currentToken.row, currentToken.column))
            }
            if (currentToken.category != Category.FIN_SENTENCIA) {
           errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
            }
            var endSentence = currentToken
            nextToken()
            return Return(reservedWord, expression, endSentence)
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una lectura
     * <Lectura> :: = READ identificador <FinSentencia>
     */
    fun isRead(): Read? {
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme) == ReservedWords.READ) {
            var reservedWord = currentToken
            nextToken()
            if (currentToken.category == Category.IDENTIFICADOR) {
                var identifier = currentToken
                nextToken()
                if (currentToken.category == Category.FIN_SENTENCIA) {
                    var endSentence = currentToken
                    nextToken()
                    return Read(reservedWord, identifier, endSentence)
                } else {
                    errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                }
            } else {
                errors.add(SyntacticError("No se encontró el identificador",  currentToken.row, currentToken.column))
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un ciclo
     * <Ciclo> :: = LOOP (<ExpresionLogica>) { <Sentencias> }
     */
    fun isLoop(): Loop? {
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme)== ReservedWords.LOOP) {
            var reservedWord = currentToken
            nextToken()
            if (currentToken.category != Category.PARENTESIS_IZQUIERDO) {
                errors.add(SyntacticError("No se encontró el paréntesis izquierdo",  currentToken.row, currentToken.column))
            }
            var parenthesisLeft = currentToken
            nextToken()
            var logicalExpression = isLogicalExpression()
            if (logicalExpression == null) {
                errors.add(SyntacticError("No se encontró la expresión lógica",  currentToken.row, currentToken.column))
            }
            if (currentToken.category != Category.PARENTESIS_DERECHO) {
                errors.add(SyntacticError("No se encontró el paréntesis derecho",  currentToken.row, currentToken.column))
            }
            var parenthesisRight = currentToken
            nextToken()

            if (currentToken.category != Category.LLAVE_IZQUIERDA) {
                errors.add(SyntacticError("No se encontró la llave izquierda",  currentToken.row, currentToken.column))
            }
            var keyLeft = currentToken
            nextToken()
            var sentences = isSentences()
            if (currentToken.category != Category.LLAVE_DERECHA) {
                errors.add(SyntacticError("No se encontró la llave derecha",  currentToken.row, currentToken.column))
            }
            var keyRight = currentToken
            nextToken()
            return Loop(reservedWord,parenthesisLeft,logicalExpression,parenthesisRight,keyLeft,sentences,keyRight)
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un incremento
     * <Incremento> :: identificador ++ <FinSentencia>
     */
    fun isIncrease(): Increase?{
        if (currentToken.category == Category.IDENTIFICADOR) {
            var identifier = currentToken
            var auxPosition = currentPosition
            nextToken()
            if (currentToken.category == Category.INCREMENTO) {
                var operator = currentToken
                nextToken()
                if (currentToken.category == Category.FIN_SENTENCIA) {
                    var endSentence = currentToken
                    nextToken()
                    return Increase(identifier, operator, endSentence)
                }else{
                    errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                }
            } else {
                backtracking(auxPosition)
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es un decremento
     * <Decremento> :: identificador -- <FinSentencia
     */
    fun isDecrement(): Decrement?{
        if (currentToken.category == Category.IDENTIFICADOR) {
            var identifier = currentToken
            nextToken()
            if (currentToken.category == Category.DECREMENTO) {
                var operator = currentToken
                nextToken()
                if (currentToken.category == Category.FIN_SENTENCIA) {
                    var endSentence = currentToken
                    nextToken()
                    return Decrement(identifier, operator, endSentence)
                }else{
                    errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
                }
            } else {
                errors.add(SyntacticError("No se encontró el operador de decremento",  currentToken.row, currentToken.column))
            }
        }
        return null
    }

    /**
     * Función encargada de verificar si una categoría es una repetición
     * <Repeticion> :: = REPEAT({[<Sentencias>]},<Entero>) <FinSentencia>
     */
    fun isRepeat(): Repeat?{
        if (currentToken.category == Category.PALABRA_RESERVADA && ReservedWords.valueOf(currentToken.lexeme)== ReservedWords.REPEAT) {
            var reservedWord = currentToken
            nextToken()
            if (currentToken.category != Category.PARENTESIS_IZQUIERDO) {
                errors.add(SyntacticError("No se encontró el paréntesis izquierdo",  currentToken.row, currentToken.column))
            }
            var parenthesisLeft = currentToken
            nextToken()
            if (currentToken.category != Category.LLAVE_IZQUIERDA) {
                errors.add(SyntacticError("No se encontró la llave izquierda",  currentToken.row, currentToken.column))
            }
            var keyLeft = currentToken
            nextToken()
            var sentences = isSentences()
            if (currentToken.category != Category.LLAVE_DERECHA) {
                errors.add(SyntacticError("No se encontró la llave derecha",  currentToken.row, currentToken.column))
            }
            var keyRight = currentToken
            nextToken()
            if (currentToken.category != Category.SEPARADOR) {
                errors.add(SyntacticError("No se encontró el separador",  currentToken.row, currentToken.column))
            }
            var separator = currentToken
            nextToken()
            if (currentToken.category != Category.ENTERO) {
                errors.add(SyntacticError("No se encontró el número de repeticiones",  currentToken.row, currentToken.column))
            }
            var repetitions = currentToken
            nextToken()
            if (currentToken.category != Category.PARENTESIS_DERECHO) {
                errors.add(SyntacticError("No se encontró el paréntesis derecho",  currentToken.row, currentToken.column))
            }
            var parenthesisRight = currentToken
            nextToken()
            if (currentToken.category != Category.FIN_SENTENCIA) {
                errors.add(SyntacticError("No se encontró el fin de sentencia",  currentToken.row, currentToken.column))
            }
            var endSentence = currentToken
            nextToken()
            return Repeat(reservedWord, parenthesisLeft, keyLeft, sentences, keyRight, separator, repetitions, parenthesisRight, endSentence)
        }
        return null
    }
}