package co.edu.uniquindio.compilador.controllers

import co.edu.uniquindio.compilador.lexical.LexicalAnalyzer
import javafx.fxml.FXML

class CompiladorController {

    private lateinit var lexicalAnalyzer: LexicalAnalyzer

    @FXML
    private fun analyze() {
        lexicalAnalyzer = LexicalAnalyzer("+===DOUBLE-=.10==/=1.0")
        lexicalAnalyzer.analyze()
    }
}