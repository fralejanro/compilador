package co.edu.uniquindio.compilador.semantic

/**
 * Clase que representa la tabla de símbolos
 * @author Francisco Alejandro Hoyos Rojas
 */
class SymbolsTable(var semanticErrors: ArrayList<SemanticError>) {
    var symbols: ArrayList<Symbol> = ArrayList()

    /**
     * Función encargada de agregar los simbolos de tipo valor en la tabla de símbolos
     */
    fun addValueSymbol(name: String, type: String?, ambit: Symbol, row: Int, column: Int): Symbol? {
        val symbol = getValueSymbol(name, ambit)
        if (symbol == null) {
            val symbolTem = Symbol(name, type, true, ambit, row, column)
            symbols.add(symbolTem)
            return symbolTem
        } else {
            semanticErrors.add(
                SemanticError(
                    "La variable de nombre$name  ya se encuentra en el ambito ${ambit.name!!}",
                    row,
                    column
                )
            )
        }
        return null
    }

    /**
     * Función encargada de agregar los simbolos de tipo función en la tabla de símbolos
     */
    fun addSymbolFunction(name: String, type: String?, typeParameters: ArrayList<String>, ambit: Symbol, row: Int, column: Int): Symbol? {
        var symbol = getSymbolFunction(name, typeParameters)
        if (symbol == null) {
            var symbolTem = Symbol(name, type, typeParameters, row, column)
            symbols.add(symbolTem)
            return symbolTem
        } else {
            semanticErrors.add(SemanticError(
                    "La función de nombre $name de parámetros ($typeParameters) ya se encuentra",
                    row,
                    column
                )
            )
        }
        return null
    }

    /**
     * Funcion encargada de obtener un valor en la tabla de simbolos
     */
    fun getValueSymbol(name: String, ambit: Symbol): Symbol? {
        for (symbol in symbols) {
            if (symbol.ambit != null) {
                if (name == symbol.name && ambit.name!! == symbol.ambit!!.name) {
                    if (ambit.typeParameters!!.size == symbol.ambit!!.typeParameters!!.size) {
                        var count = 0
                        while (count < ambit.typeParameters!!.size && ambit.typeParameters!![count] == symbol.ambit!!.typeParameters!![count]) {
                            count += 1
                        }
                        if (count == ambit.typeParameters!!.size) {
                            return symbol
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * Funcion encargada de obtener una función en la tabla de simbolos
     */
    fun getSymbolFunction(name: String, typeParameters: ArrayList<String>): Symbol? {
        for (symbol in symbols) {
            if (symbol.typeParameters != null) {
                if (name == symbol.name && typeParameters == symbol.typeParameters) {
                    return symbol
                }
            }
        }
        return null
    }
}