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
import javafx.stage.Stage;

import java.io.IOException;

public class CarrinhoController {

    @FXML private ListView<ItemCarrinho> listaItensCarrinho;
    @FXML private Label labelTotalFinal;
    @FXML private Label labelStatus;
    @FXML private Button botaoFinalizar;

    private Cliente clienteLogado;
    private Carrinho carrinho;

    @FXML
    public void initialize() {
        clienteLogado = SessaoManager.getInstance().getClienteLogado();
        carrinho = SessaoManager.getInstance().getCarrinho();

        listaItensCarrinho.setItems(FXCollections.observableArrayList(carrinho.getItens()));
        labelTotalFinal.setText(String.format("Total: R$ %.2f", carrinho.calcularTotal()));
    }

    @FXML
    protected void handleFinalizar() {
        if (carrinho.getItens().isEmpty()) {
            labelStatus.setText("Carrinho está vazio!");
            labelStatus.setStyle("-fx-text-fill: red;");
            return;
        }

        Pedido novoPedido = new Pedido(clienteLogado, carrinho);
        boolean sucesso = Repositorio.salvarPedido(novoPedido);

        if (sucesso) {
            SessaoManager.getInstance().novoCarrinho();

            listaItensCarrinho.setItems(null);
            labelStatus.setText("Pedido #" + novoPedido.getId() + " finalizado com sucesso!");
            labelStatus.setStyle("-fx-text-fill: #27ae60;"); // Verde
            labelTotalFinal.setText("Total: R$ 0,00");
            botaoFinalizar.setDisable(true);
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
            // REAPLICA O CSS
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}