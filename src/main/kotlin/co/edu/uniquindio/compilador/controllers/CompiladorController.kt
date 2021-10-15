package co.edu.uniquindio.compilador.controllers

import co.edu.uniquindio.compilador.lexical.LexicalAnalyzer
import co.edu.uniquindio.compilador.lexical.LexicalError
import co.edu.uniquindio.compilador.lexical.Token
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*

class CompiladorController: Initializable {

    private lateinit var lexicalAnalyzer: LexicalAnalyzer
    @FXML
    lateinit var txtCodigo: TextArea
    @FXML
    lateinit var tablaPrincipal: TableView<Token>
    @FXML
    lateinit var colRow: TableColumn<Token, String>
    @FXML
    lateinit var colColumn: TableColumn<Token, String>
    @FXML
    lateinit var colCategory: TableColumn<Token, String>
    @FXML
    lateinit var colLexeme: TableColumn<Token, String>
    @FXML
    lateinit var tablaError: TableView<LexicalError>
    @FXML
    lateinit var colError: TableColumn<LexicalError, String>
    @FXML
    lateinit var colRowError: TableColumn<LexicalError, String>
    @FXML
    lateinit var colColumnError: TableColumn<LexicalError, String>

    @FXML
    private fun analyze() {
        if (txtCodigo.text.length > 0) {
            lexicalAnalyzer = LexicalAnalyzer(txtCodigo.text)
            lexicalAnalyzer.analyze()
            tablaPrincipal.items = FXCollections.observableArrayList(lexicalAnalyzer.tokens)
        }else{
            var alerta = Alert(Alert.AlertType.WARNING)
            alerta.headerText = "Info"
            alerta.contentText = "No ha ingresado ningún código fuente para analizar"
            alerta.show()
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        colRow.cellValueFactory = PropertyValueFactory("row")
        colColumn.cellValueFactory = PropertyValueFactory("column")
        colCategory.cellValueFactory = PropertyValueFactory("category")
        colLexeme.cellValueFactory = PropertyValueFactory("lexeme")
    }
}