package co.edu.uniquindio.compilador.semantic

/**
 * Clase que representa un símbolo
 * @author Francisco Alejandro Hoyos Rojas
 */
class Symbol {

    var name: String? = ""
    var type: String? = ""
    var modifiable: Boolean = false
    var row: Int = 0
    var column: Int = 0
    var ambit: Symbol? = null
    var typeParameters: ArrayList<String>? = null

    /**
     * Constructor para crear un simbolo de tipo valor
     */
    constructor(name: String?, type: String?, modifiable: Boolean, ambit: Symbol?, row: Int, column: Int){
        this.name = name
        this.type = type
        this.modifiable = modifiable
        this.ambit = ambit
        this.row = row
        this.column = column
    }

    /**
     * Constructor para crear un simbolo de tipo método
     */
    constructor(name: String, type: String?, typeParameters: ArrayList<String>, row: Int, column: Int){
        this.name = name
        this.type = type
        this.typeParameters = typeParameters
        this.row = row
        this.column = column
    }

}