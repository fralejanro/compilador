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
     * Atributo que representa algunos caracteres especiales
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
     */
    fun addToken(lexeme : String, category : Category, row : Int, column : Int) : Boolean{
        tokens.add(Token(lexeme, category, row, column ))
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

            addToken(currentCharacter.toString(), Category.DESCONOCIDO, currentRow, currentColumn)
            nextCharacter()
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
                    backtracking()
                    return false
                }
                nextCharacter()
            }
            return addToken(lexeme, Category.ENTERO, positionsBacktracking[0], positionsBacktracking[1])
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
                    backtracking()
                    return false
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
                backtracking()
                return false
            }
            return addToken(lexeme, Category.DECIMAL, currentRow, currentColumn)
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
                addToken(lexeme, Category.PALABRA_RESERVADA, currentRow, currentColumn)
                return true
            }else{
                //TODO MAX 10 CARACTERES
                return addToken(lexeme,Category.IDENTIFICADOR,currentRow,currentColumn)
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
            false
        }else{
            addToken(lexeme,Category.OPERADOR_ARITMETICO, currentRow, currentColumn)
        }
    }

    /**
     * Función encargada de validar si un carácter es un punto
     */
    fun isPoint(character: Char) : Boolean{
        return character == '.'
    }

    /**
     * Función encargada de validar si un carácter es un guion bajo
     */
    fun isUnderscore(character: Char) : Boolean{
        return character == '_'
    }

    /**
     * Función encargada de concatenar el carácter actual
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
     */
    fun backtracking (){
        currentRow = positionsBacktracking[0]
        currentColumn = positionsBacktracking[1]
        currentPosition = positionsBacktracking[2]
        currentCharacter = sourceCode[positionsBacktracking[2]]
    }
}