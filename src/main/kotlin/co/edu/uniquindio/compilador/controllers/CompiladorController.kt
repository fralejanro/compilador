package co.edu.uniquindio.compilador.controllers

import co.edu.uniquindio.compilador.lexical.LexicalAnalyzer
import co.edu.uniquindio.compilador.lexical.LexicalError
import co.edu.uniquindio.compilador.lexical.Token
import co.edu.uniquindio.compilador.syntactic.CompilationUnit
import co.edu.uniquindio.compilador.syntactic.SyntacticAnalyzer
import co.edu.uniquindio.compilador.syntactic.SyntacticError
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*

class CompiladorController: Initializable {

    private lateinit var lexicalAnalyzer: LexicalAnalyzer
    private lateinit var syntacticAnalyzer: SyntacticAnalyzer
    private lateinit var compilationUnit : CompilationUnit
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
    lateinit var tableError: TableView<LexicalError>
    @FXML
    lateinit var colError: TableColumn<LexicalError, String>
    @FXML
    lateinit var colRowError: TableColumn<LexicalError, String>
    @FXML
    lateinit var colColumnError: TableColumn<LexicalError, String>
    @FXML
    lateinit var tableErrorSyntactic: TableView<SyntacticError>
    @FXML
    lateinit var colErrorSyntactic: TableColumn<SyntacticError, String>
    @FXML
    lateinit var colRowErrorSyntactic: TableColumn<SyntacticError, String>
    @FXML
    lateinit var colColumnErrorSyntactic: TableColumn<SyntacticError, String>
    @FXML
    lateinit var tree: TreeView<String>

    @FXML
    private fun analyze() {
        tablaPrincipal.items.clear()
        tableError.items.clear()
        tableErrorSyntactic.items.clear()
        tree.root = null
        if (txtCodigo.text.length > 0) {
            lexicalAnalyzer = LexicalAnalyzer(txtCodigo.text)
            lexicalAnalyzer.analyze()
            tablaPrincipal.items = FXCollections.observableArrayList(lexicalAnalyzer.tokens)
            tableError.items = FXCollections.observableArrayList(lexicalAnalyzer.errors)
            if (lexicalAnalyzer.errors.isEmpty()) {
                syntacticAnalyzer = SyntacticAnalyzer(lexicalAnalyzer.tokens)
                compilationUnit = syntacticAnalyzer.isCompilationUnit()!!
                if (syntacticAnalyzer.errors.isEmpty() && (compilationUnit.functions.size>0 || compilationUnit.globalVariables.size>0)) {
                    tree.root = compilationUnit.getTree()
                }else{
                    tableErrorSyntactic.items = FXCollections.observableArrayList(syntacticAnalyzer.errors)
                }
            }
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
        colRowError.cellValueFactory = PropertyValueFactory("row")
        colColumnError.cellValueFactory = PropertyValueFactory("column")
        colError.cellValueFactory = PropertyValueFactory("message")
        colRowErrorSyntactic.cellValueFactory = PropertyValueFactory("row")
        colColumnErrorSyntactic.cellValueFactory = PropertyValueFactory("column")
        colErrorSyntactic.cellValueFactory = PropertyValueFactory("message")

    }
}