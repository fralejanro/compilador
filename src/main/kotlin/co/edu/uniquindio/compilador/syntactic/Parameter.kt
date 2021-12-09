package co.edu.uniquindio.compilador.syntactic

import co.edu.uniquindio.compilador.lexical.Token
import javafx.scene.control.TreeItem

/**
 * Clase que representa un parámetro
 * @author Francisco Alejandro Hoyos Rojas
 */
class Parameter (var dataType: Token, var name: Token) {

    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Parameter")
        parent.children.add(TreeItem("Data type: ${dataType.lexeme}"))
        parent.children.add(TreeItem("Identifier: ${name.lexeme}"))
        return parent
    }

    /**
     * Función encargada de obtener el código java
     */
    fun getJavaCode():String{
        return dataType.getJavaCode() +" "+name.lexeme
    }
}