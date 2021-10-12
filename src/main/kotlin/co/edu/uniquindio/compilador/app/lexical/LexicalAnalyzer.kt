package co.edu.uniquindio.compilador.app.lexical

/**
 * Clase que representa el analizador léxico
 * @author Francisco Alejandro Hoyos Rojas
 */
class LexicalAnalyzer(var sourceCode : String ) {

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
    
}