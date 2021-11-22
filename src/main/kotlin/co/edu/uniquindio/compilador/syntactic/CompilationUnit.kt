package co.edu.uniquindio.compilador.syntactic

import javafx.scene.control.TreeItem

/**
 * Clase que representa la unidad de compilación
 * @author Francisco Alejandro Hoyos Rojas
 */
class CompilationUnit (var functions : ArrayList<Funcion>, var globalVariables : ArrayList<GlobalVariableDeclaration> ) {

    fun getTree(): TreeItem<String> {
        var parent = TreeItem("Compilation Unit")
        var parentFunctions = TreeItem("Functions")
        for (funcion in functions) {
            parentFunctions.children.add(funcion.getTree())
        }
        var parentGlobalVariables = TreeItem("Global Variables")
        for (globalVariable in globalVariables){
            parentGlobalVariables.children.add(globalVariable.getTree())
        }
        parent.children.add(parentFunctions)
        parent.children.add(parentGlobalVariables)
        return parent
    }

    override fun toString(): String {
        return "CompilationUnit(functions=$functions)"
    }
}