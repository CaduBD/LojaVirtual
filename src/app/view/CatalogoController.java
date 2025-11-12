package app.view;

import app.db.Repositorio;
import app.model.Carrinho;
import app.model.Cliente;
import app.model.Produto;
import app.util.SessaoManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CatalogoController {

    @FXML private Label labelBoasVindas;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private TableColumn<Produto, Integer> colunaEstoque;
    @FXML private TextField campoQuantidade;
    @FXML private Label labelTotalCarrinho;
    @FXML private Button botaoVerCarrinho;
    @FXML private Label labelErro;

    private Cliente clienteLogado;
    private Carrinho carrinho;

    @FXML
    public void initialize() {
        clienteLogado = SessaoManager.getInstance().getClienteLogado();
        carrinho = SessaoManager.getInstance().getCarrinho();

        if (clienteLogado == null) return;

        labelBoasVindas.setText("Olá, " + clienteLogado.getNome() + "!");

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        carregarProdutos();
        atualizarTotalCarrinho();
    }

    private void carregarProdutos() {
        tabelaProdutos.setItems(FXCollections.observableArrayList(Repositorio.listarProdutos()));
    }

    @FXML
    protected void handleAdicionar() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        labelErro.setText(""); // Limpa erro anterior

        if (produtoSelecionado == null) {
            labelErro.setText("Selecione um produto na tabela.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText());
            if (quantidade <= 0) {
                labelErro.setText("Quantidade deve ser positiva.");
                return;
            }
        } catch (NumberFormatException e) {
            labelErro.setText("Quantidade inválida.");
            return;
        }

        Optional<Produto> produtoRealOpt = Repositorio.listarProdutos().stream()
                .filter(p -> p.getId() == produtoSelecionado.getId())
                .findFirst();

        int estoqueReal = produtoRealOpt.map(Produto::getQuantidadeEstoque).orElse(0);

        if (quantidade > estoqueReal) {
            labelErro.setText("Estoque insuficiente. Restam apenas: " + estoqueReal);
            return;
        }

        carrinho.adicionarProduto(produtoSelecionado, quantidade);
        atualizarTotalCarrinho();
        labelErro.setText(""); // Sucesso
    }

    private void atualizarTotalCarrinho() {
        labelTotalCarrinho.setText(String.format("Total: R$ %.2f", carrinho.calcularTotal()));
    }

    @FXML
    protected void handleVerCarrinho() {
        mudarTela("CarrinhoView.fxml");
    }

    private void mudarTela(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) botaoVerCarrinho.getScene().getWindow();
            Scene scene = new Scene(root);
            // REAPLICA O CSS
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            labelErro.setText("Erro ao carregar tela.");
        }
    }
}