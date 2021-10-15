package co.edu.uniquindio.compilador.lexical

/**
 * Clase que representa el analizador léxico
 * @author Francisco Alejandro Hoyos Rojas
 */
class LexicalAnalyzer(var sourceCode : String ) {

    /**
     * Atributo que representa las posiciones para hacer backtracking
     *
     * 0 - row
     * 1 - column
     * 2 - position
     */
    var positionsBacktracking = mutableListOf(0,0,0)

    /**
     * Atributo que representa algunos carácteres especiales
     */
    var specialCharacters = listOf(' ','\n', '\t')

    /**
     * Atributo que representa la posición actual dentro del código fuente
     */
    var currentPosition = 0

    /**
     * Atributo que representa la posición actual dentro del código fuente
     */
    var currentCharacter = sourceCode[0]

    /**
     * Atributo que representa la lista de tokens
     */
    var tokens = ArrayList<Token>()

    /**
     * Atributo que representa la fila actual
     */
    var currentRow = 0

    /**
     * Atributo que representa la columna actual
     */
    var currentColumn = 0

    /**
     * Atributo que representa el fin del código fuente
     */
    var endSourceCode = 0.toChar()

    /**
     * Función encargada de agregar un token a la lista de tokens
     * @param lexeme lexema del token
     * @param category categoría del token
     * @return true
     */
    fun addToken(lexeme : String, category : Category) : Boolean{
        tokens.add(Token(lexeme, category, positionsBacktracking[0], positionsBacktracking[1]))
        return true
    }


    /**
     * Función encargada de agregar un token a la lista de tokens y de obtener el siguiente token
     * @param lexeme lexema del token
     * @param category categoría del token
     * @return true
     */
    fun addTokenNext(lexeme : String, category : Category) : Boolean{
        tokens.add(Token(lexeme, category, currentRow, currentColumn))
        nextCharacter()
        return true
    }

    /**
     * Función encargada de aumentar la posición actual de la fila y la columna actual
     * para obtener el siguiente carácter
     */
    fun nextCharacter(){
        if(currentPosition == sourceCode.length -1){
            currentCharacter = endSourceCode
        }else{
            if(currentCharacter == specialCharacters[1]){
                currentRow++
                currentColumn = 0
            }else{
                currentColumn++
            }
            currentPosition++
            currentCharacter = sourceCode[currentPosition]
        }
    }

    /**
     * Función encargada de analizar el código fuente
     */
    fun analyze(){
        while(currentCharacter != endSourceCode) {
            if (specialCharacters.contains(currentCharacter)) {
                nextCharacter()
                continue
            }
            if(isInteger()) continue
            if(isReservedWordsOrIdentifier()) continue
            if(isDecimal()) continue
            if(isArithmeticOperator()) continue
            if(isAssignment()) continue
            if(isIncreaseOrDecrement('+')) continue
            if(isIncreaseOrDecrement('-')) continue
            if(isRelationalOperator()) continue
            if(isAnotherCharacter(',',Category.SEPARADOR)) continue
            if(isAnotherCharacter(':',Category.DOS_PUNTOS)) continue
            if(isAnotherCharacter('{',Category.LLAVE_IZQUIERDA)) continue
            if(isAnotherCharacter('}',Category.LLAVE_DERECHA)) continue
            if(isAnotherCharacter('[',Category.CORCHETE_IZQUIERDO)) continue
            if(isAnotherCharacter(']',Category.CORCHETE_DERECHO)) continue
            if(isAnotherCharacter('(',Category.PARENTESIS_IZQUIERDO)) continue
            if(isAnotherCharacter(')',Category.PARENTESIS_DERECHO)) continue
            if(isAnotherCharacter(';',Category.FIN_SENTENCIA)) continue
            addTokenNext(currentCharacter.toString(), Category.DESCONOCIDO)
        }
    }

    /**
     * Función encargada de verificar si un token es entero
     * @return true si el token es entero; de lo contrario, false
     */
    fun isInteger() : Boolean{
        if(currentCharacter.isDigit()){
            var lexeme = ""
            lexeme = concatCurrentCharacter(lexeme)
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            nextCharacter()
            while (currentCharacter.isDigit() || isPoint(currentCharacter)){
                lexeme = concatCurrentCharacter(lexeme)
                if(isPoint(currentCharacter)){
                    return backtracking()
                }
                nextCharacter()
            }
            return addToken(lexeme, Category.ENTERO)
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es decimal
     * @return true si el token es entero; de lo contrario, false
     */
    fun isDecimal() : Boolean{
        if(currentCharacter.isDigit() || isPoint(currentCharacter)){
            var lexeme = ""
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            if(isPoint(currentCharacter)){
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
                if(currentCharacter.isDigit()){
                    lexeme = concatCurrentCharacter(lexeme)
                    nextCharacter()
                }else{
                    return backtracking()
                }
            }else{
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
                while(currentCharacter.isDigit()){
                    lexeme = concatCurrentCharacter(lexeme)
                    nextCharacter()
                }
                if(isPoint(currentCharacter)){
                    lexeme = concatCurrentCharacter(lexeme)
                    nextCharacter()
                }
            }

            while(currentCharacter.isDigit()){
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
            }

            if(isPoint(currentCharacter)){
                return backtracking()
            }
            return addToken(lexeme, Category.DECIMAL)
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es una palabra reservada o un identificador
     * @return true si el token es es una palabra reservada o un identificador; de lo contrario, false
     */
    fun isReservedWordsOrIdentifier() : Boolean{
        if(currentCharacter.isLetter() ||  isUnderscore(currentCharacter)){
            var lexeme = ""
            lexeme = concatCurrentCharacter(lexeme)
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            nextCharacter()
            while(currentCharacter.isLetter() || currentCharacter.isDigit() || isUnderscore(currentCharacter)){
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
            }
            if(ReservedWords.values().map { it.name }.contains(lexeme)){
                addToken(lexeme, Category.PALABRA_RESERVADA)
                return true
            }else{
                //TODO MAX 10 carácterES
                return addToken(lexeme,Category.IDENTIFICADOR)
            }
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es un operador aritmético
     * @return true si el token es es un operador aritmético; de lo contrario, false
     */
    fun isArithmeticOperator(): Boolean{
        when(currentCharacter){
            '+' -> return addTokenArithmeticOperator(mutableListOf('+','='))
            '-' -> return addTokenArithmeticOperator(mutableListOf('-','='))
            '/' -> return addTokenArithmeticOperator(mutableListOf('-','=','*'))
            '*','%' -> return addTokenArithmeticOperator(mutableListOf('='))
            else -> return false
        }
    }

    /**
     * Función encargada de agregar un token de un operador aritmético
     * @return true si se agrega el operador aritmético; de lo contrario, false
     */
    fun addTokenArithmeticOperator(characters: MutableList<Char>): Boolean{
        var lexeme =  ""
        lexeme = concatCurrentCharacter(lexeme)
        setPositionsBacktracking(currentRow,currentColumn,currentPosition)
        nextCharacter()
        return if(characters.contains(currentCharacter)){
            backtracking()
        }else{
            addToken(lexeme,Category.OPERADOR_ARITMETICO)
        }
    }

    /**
     * Función encargada de verificar si un token es asignación
     * @return true si el token es asignación; de lo contrario, false
     */
    fun isAssignment() : Boolean{
        if(mutableListOf('=','+','-','*','/').contains(currentCharacter)){
            var lexeme = ""
            val previousCharacter = currentCharacter
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            lexeme = concatCurrentCharacter(lexeme)
            nextCharacter()
            return if(isEquals(previousCharacter) && isEquals(currentCharacter)){
                // Flujo =
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
                if(isEquals(currentCharacter)){
                    backtracking()
                }else{
                    addToken(lexeme, Category.ASIGNACION)
                }
            }else if(isEquals(currentCharacter)){
                // Flujo +, -, *, /
                lexeme = concatCurrentCharacter(lexeme)
                nextCharacter()
                addToken(lexeme, Category.ASIGNACION)
            }else{
                backtracking()
            }
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es incremento o decremento
     * @param operator operador a validar
     * @return true si el token es incremento o decremento; de lo contrario, false
     */
    fun isIncreaseOrDecrement(operator : Char): Boolean {
        if(currentCharacter==operator){
            var lexeme = ""
            setPositionsBacktracking(currentRow, currentColumn, currentPosition)
            lexeme = concatCurrentCharacter(lexeme)
            nextCharacter()
            if(currentCharacter==operator){
                lexeme = concatCurrentCharacter(lexeme)
                val category = if (isPlus(currentCharacter)) Category.INCREMENTO else Category.DECREMENTO
                nextCharacter()
                return addToken(lexeme, category)
            }
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es un operador relacional
     * @return true si el token es es un operador relacional; de lo contrario, false
     */
    fun isRelationalOperator(): Boolean{
        if(mutableListOf('!','=','<','>').contains(currentCharacter)) {
            var lexeme = ""
            setPositionsBacktracking(currentRow, currentColumn, currentPosition)
            lexeme = concatCurrentCharacter(lexeme)
            if (currentCharacter == '<' || currentCharacter == '>') {
                nextCharacter()
                if (isEquals(currentCharacter)) {
                    lexeme = concatCurrentCharacter(lexeme)
                    return addTokenNext(lexeme, Category.OPERADOR_RELACIONAL)
                }
                return addToken(lexeme, Category.OPERADOR_RELACIONAL)
            } else {
                nextCharacter()
                if (isEquals(currentCharacter)) {
                    lexeme = concatCurrentCharacter(lexeme)
                    nextCharacter()
                    if (isEquals(currentCharacter)) {
                        lexeme = concatCurrentCharacter(lexeme)
                        return addTokenNext(lexeme, Category.OPERADOR_RELACIONAL)
                    } else {
                        return addToken(lexeme, Category.OPERADOR_RELACIONAL)
                    }

                } else {
                    return backtracking()
                }
            }
        }
        return false
    }

    /**
     * Función encargada de verificar si un token es de un solo carácter
     * @param character caracter a validar
     * @param category categoría del token
     * @return true si el token es de un solo carácter; de lo contrario, false
     */
    fun isAnotherCharacter (character:Char,category:Category): Boolean{
        if(currentCharacter==character){
            var lexeme = ""
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            lexeme = concatCurrentCharacter(lexeme)
            return addTokenNext(lexeme,category)
        }else{
            return false
        }
    }

    /**
     * Función encargada de validar si un carácter es una suma
     * @param character carácter a validar
     * @return true si el carácter es una suma; de lo contrario, false
     */
    fun isPlus(character: Char) : Boolean{
        return character == '+'
    }

    /**
     * Función encargada de validar si un carácter es un igual
     * @param character carácter a validar
     * @return true si el carácter es un igual; de lo contrario, false
     */
    fun isEquals(character: Char) : Boolean{
        return character == '='
    }

    /**
     * Función encargada de validar si un carácter es un punto
     * @param character carácter a validar
     * @return true si el carácter es un punto; de lo contrario, false
     */
    fun isPoint(character: Char) : Boolean{
        return character == '.'
    }

    /**
     * Función encargada de validar si un carácter es un guión bajo
     * @param character carácter a validar
     * @return true si el carácter es un guión bajo; de lo contrario, false
     */
    fun isUnderscore(character: Char) : Boolean{
        return character == '_'
    }

    /**
     * Función encargada de concatenar el carácter actual
     * @param lexeme lexema a concatenar
     * @return lexema concatenado con el carácter actual
     */
    fun concatCurrentCharacter(lexeme: String) : String {
        return lexeme + currentCharacter
    }

    /**
     * Función encargada de almacenar las posiciones para hacer backtracking
     * @param row fila actual
     * @param column columna actual
     * @param position posición actual
     */
    fun setPositionsBacktracking(row: Int, column: Int, position : Int){
        positionsBacktracking[0] = row
        positionsBacktracking[1] = column
        positionsBacktracking[2] = position
    }

    /**
     * Función encargada de hacer backtracking
     * @return false
     */
    fun backtracking () : Boolean{
        currentRow = positionsBacktracking[0]
        currentColumn = positionsBacktracking[1]
        currentPosition = positionsBacktracking[2]
        currentCharacter = sourceCode[positionsBacktracking[2]]
        return false
    }
}