package app.view;

import app.db.Repositorio;
import app.model.Carrinho;
import app.model.Cliente;
import app.model.ItemCarrinho;
import app.model.Pedido;
import app.util.SessaoManager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField; // IMPORTAR TEXTFIELD
import javafx.stage.Stage;

import java.io.IOException;

public class CarrinhoController {

    @FXML private ListView<ItemCarrinho> listaItensCarrinho;
    @FXML private Label labelTotalFinal;
    @FXML private Label labelStatus;
    @FXML private Button botaoFinalizar;

    @FXML private Button botaoRemover;
    @FXML private Label labelErroRemover;
    @FXML private TextField campoQtdRemover;

    private Cliente clienteLogado;
    private Carrinho carrinho;

    @FXML
    public void initialize() {
        clienteLogado = SessaoManager.getInstance().getClienteLogado();
        carrinho = SessaoManager.getInstance().getCarrinho();
        atualizarVisualCarrinho();
    }

    /**
     * MÉTODO ATUALIZADO: Agora lê a quantidade do TextField.
     */
    @FXML
    protected void handleRemoverItem() {
        labelErroRemover.setText(""); // Limpa erro anterior

        // 1. Pega o item selecionado na lista
        ItemCarrinho itemSelecionado = listaItensCarrinho.getSelectionModel().getSelectedItem();

        if (itemSelecionado == null) {
            labelErroRemover.setText("Selecione um item para remover!");
            return;
        }

        // 2. Pega a quantidade a remover do TextField
        int quantidadeParaRemover;
        try {
            quantidadeParaRemover = Integer.parseInt(campoQtdRemover.getText());
            if (quantidadeParaRemover <= 0) {
                labelErroRemover.setText("Qtd. deve ser positiva!");
                return;
            }
        } catch (NumberFormatException e) {
            labelErroRemover.setText("Qtd. inválida!");
            return;
        }

        // 3. Validação de lógica
        if (quantidadeParaRemover > itemSelecionado.getQuantidade()) {
            labelErroRemover.setText("Você só tem " + itemSelecionado.getQuantidade() + " desse item!");
            return;
        }

        // 4. Chama o novo método do Model (Carrinho)
        carrinho.removerQuantidade(itemSelecionado.getProduto(), quantidadeParaRemover);

        // 5. Atualiza a tela (lista e total)
        atualizarVisualCarrinho();
        campoQtdRemover.setText("1"); // Reseta o campo
    }

    @FXML
    protected void handleFinalizar() {
        labelErroRemover.setText("");

        if (carrinho.getItens().isEmpty()) {
            labelStatus.setText("Carrinho está vazio!");
            labelStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        Pedido novoPedido = new Pedido(clienteLogado, carrinho);
        boolean sucesso = Repositorio.salvarPedido(novoPedido);

        if (sucesso) {
            SessaoManager.getInstance().novoCarrinho();
            atualizarVisualCarrinho();
            labelStatus.setText("Pedido #" + novoPedido.getId() + " finalizado com sucesso!");
            labelStatus.setStyle("-fx-text-fill: #27ae60;"); // Verde
            botaoFinalizar.setDisable(true);
            botaoRemover.setDisable(true);
        } else {
            labelStatus.setText("Erro ao processar o pedido.");
            labelStatus.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void handleVoltar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CatalogoView.fxml"));
            Stage stage = (Stage) botaoFinalizar.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void atualizarVisualCarrinho() {
        listaItensCarrinho.setItems(FXCollections.observableArrayList(carrinho.getItens()));
        labelTotalFinal.setText(String.format("Total: R$ %.2f", carrinho.calcularTotal()));
    }
}