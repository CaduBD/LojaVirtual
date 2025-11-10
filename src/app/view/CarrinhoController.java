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
        // Pega os dados da Sessão
        clienteLogado = SessaoManager.getInstance().getClienteLogado();
        carrinho = SessaoManager.getInstance().getCarrinho();

        // Configura a ListView (ela vai usar o .toString() do ItemCarrinho)
        listaItensCarrinho.setItems(FXCollections.observableArrayList(carrinho.getItens()));

        // Atualiza o total
        labelTotalFinal.setText(String.format("Total: R$ %.2f", carrinho.calcularTotal()));
    }

    @FXML
    protected void handleFinalizar() {
        if (carrinho.getItens().isEmpty()) {
            labelStatus.setText("Carrinho está vazio!");
            return;
        }

        // 1. Criar o objeto Pedido (Model)
        Pedido novoPedido = new Pedido(clienteLogado, carrinho);

        // 2. Salvar no "banco de dados falso" (Repositório)
        boolean sucesso = Repositorio.salvarPedido(novoPedido);

        if (sucesso) {
            // 3. Limpar o carrinho da sessão
            SessaoManager.getInstance().novoCarrinho();

            // 4. Atualizar a tela
            listaItensCarrinho.setItems(null); // Limpa a lista visual
            labelStatus.setText("Pedido #" + novoPedido.getId() + " finalizado com sucesso!");
            labelTotalFinal.setText("Total: R$ 0,00");
            botaoFinalizar.setDisable(true); // Desabilita o botão
        } else {
            labelStatus.setText("Erro ao processar o pedido.");
        }
    }

    @FXML
    protected void handleVoltar() {
        // Volta para o catálogo
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CatalogoView.fxml"));
            Stage stage = (Stage) botaoFinalizar.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}