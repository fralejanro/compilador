package co.edu.uniquindio.compilador.lexical

/**
 * Clase que representa un token
 * @author Francisco Alejandro Hoyos Rojas
 */
class Token (var lexeme : String, var category : Category, var row : Int, var column : Int) {

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode():String{
        if(category==Category.PALABRA_RESERVADA){
            when(lexeme){
                "INTEGER" -> {return "int"}
                "DOUBLE" -> {return "double"}
                "BOOLEAN" -> {return "boolean"}
                "STRING" -> {return "String"}
                "VOID" -> {return "void"}
                "TRUE" -> {return "true"}
                "FALSE" -> {return "false"}
                "IF" -> {return "if"}
                "THEN" -> {return "else"}
                "LOOP" -> {return "while"}
                "RETURN" -> {return "return"}
                "PRINT" -> {return "JOptionPane.showMessageDialog(null,"}
            }
        }else if(category==Category.FIN_SENTENCIA){
            return ";"
        }
        return "INDEFINIDO"
    }
}