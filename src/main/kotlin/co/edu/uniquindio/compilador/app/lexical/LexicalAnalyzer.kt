package co.edu.uniquindio.compilador.app.lexical

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
    var endSourceCode = 0.toChar();

    /**
     * Función encargada de agregar un token a la lista de tokens
     */
    fun addToken(lexeme : String, category : Category, row : Int, column : Int){
        tokens.add(Token(lexeme, category, row, column ))
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
     * Función encarga de analizar el código fuente
     */
    fun analyze(){
        while(currentCharacter != endSourceCode){
            if(specialCharacters.contains(currentCharacter)) {
                nextCharacter()
                continue
            }
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
            lexeme += currentCharacter
            setPositionsBacktracking(currentRow,currentColumn,currentPosition)
            nextCharacter()
            while (currentCharacter.isDigit() || currentCharacter == '.'){
                lexeme += currentCharacter
                if(currentCharacter=='.'){
                    backtracking()
                    return false
                }
                nextCharacter()
            }
            addToken(lexeme, Category.ENTERO, positionsBacktracking[0], positionsBacktracking[1])
            return true
        }
        return true
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