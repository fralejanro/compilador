package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa un valor numérico
 * @author Francisco Alejandro Hoyos Rojas
 */
class NumericalValue(var symbol: Token?, var  type:Token) {

    /**
     * Función encargada de obtener el árbol visual
     */
    fun getTree(): TreeItem<String> {
        return TreeItem("Type: ${type.lexeme} : symbol: ${symbol?.lexeme} ")
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode():String{
        var sourceCode=""
        if(symbol!=null){
            sourceCode+=symbol?.lexeme+type.lexeme
        }
        sourceCode+=type.lexeme
        return sourceCode
    }


}