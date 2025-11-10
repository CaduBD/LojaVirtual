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

    private ObservableList<Produto> listaProdutos;

    @FXML
    public void initialize() {
        // 1. Pega os dados da Sessão
        clienteLogado = SessaoManager.getInstance().getClienteLogado();
        carrinho = SessaoManager.getInstance().getCarrinho();

        if (clienteLogado == null) {
            // Isso não deveria acontecer, mas é uma defesa
            labelBoasVindas.setText("Erro: Cliente não logado.");
            return;
        }

        labelBoasVindas.setText("Bem-vindo(a), " + clienteLogado.getNome() + "!");

        // 2. Configura a Tabela (deve corresponder aos getters do Produto.java)
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        // 3. Carrega os produtos do banco falso
        carregarProdutos();

        // 4. Atualiza o total (caso o usuário volte do carrinho)
        atualizarTotalCarrinho();
    }

    private void carregarProdutos() {
        listaProdutos = FXCollections.observableArrayList(Repositorio.listarProdutos());
        tabelaProdutos.setItems(listaProdutos);
    }

    @FXML
    protected void handleAdicionar() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

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

        // Validação de estoque (verifica o estoque real, não o do carrinho)
        Optional<Produto> produtoRealOpt = Repositorio.listarProdutos().stream()
                .filter(p -> p.getId() == produtoSelecionado.getId())
                .findFirst();

        int estoqueReal = 0;
        if (produtoRealOpt.isPresent()) {
            estoqueReal = produtoRealOpt.get().getQuantidadeEstoque();
        }

        if (quantidade > estoqueReal) {
            labelErro.setText("Estoque insuficiente. Máx: " + estoqueReal);
            return;
        }

        // Adiciona ao carrinho (Model)
        carrinho.adicionarProduto(produtoSelecionado, quantidade);

        // Atualiza a View
        atualizarTotalCarrinho();
        labelErro.setText(produtoSelecionado.getNome() + " adicionado!");
    }

    private void atualizarTotalCarrinho() {
        labelTotalCarrinho.setText(String.format("Total: R$ %.2f", carrinho.calcularTotal()));
    }

    @FXML
    protected void handleVerCarrinho() {
        mudarTela("CarrinhoView.fxml");
    }

    // Método auxiliar para mudar de tela
    private void mudarTela(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) botaoVerCarrinho.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            labelErro.setText("Erro ao carregar o carrinho.");
        }
    }
}