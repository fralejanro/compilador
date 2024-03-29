package co.edu.uniquindio.compilador.lexical

/**
 * Enumeración que representa una categoría léxica
 * @author Francisco Alejandro Hoyos Rojas
 */
enum class Category {
    IDENTIFICADOR, PALABRA_RESERVADA, ENTERO, DECIMAL, OPERADOR_ARITMETICO, ASIGNACION, INCREMENTO, DECREMENTO,
    OPERADOR_RELACIONAL, OPERADOR_LOGICO, LLAVE_IZQUIERDA, LLAVE_DERECHA, PARENTESIS_IZQUIERDO, PARENTESIS_DERECHO,
    CORCHETE_DERECHO, CORCHETE_IZQUIERDO, FIN_SENTENCIA, PUNTO, DOS_PUNTOS, SEPARADOR, COMENTARIO_LINEA,
    COMENTARIO_BLOQUE, CADENA, CARACTER, DESCONOCIDO, FIN_CODIGO, ERROR
}