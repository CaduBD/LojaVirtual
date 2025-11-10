package app.view;

import app.db.Repositorio;
import app.model.Cliente;
import app.util.SessaoManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private Button botaoEntrar;

    /**
     * Chamado quando o botão "Entrar na Loja" é clicado.
     */
    @FXML
    protected void handleEntrar() {
        // 1. Pega o cliente padrão do nosso repositório
        Cliente cliente = Repositorio.getClientePadrao();

        if (cliente != null) {
            // 2. Guarda o cliente na sessão global
            SessaoManager.getInstance().setClienteLogado(cliente);

            // 3. Navega para a tela de Catálogo
            mudarTela("CatalogoView.fxml");
        } else {
            // Se isso acontecer, algo deu errado na inicialização do Repositorio
            System.err.println("Nenhum cliente padrão encontrado!");
        }
    }

    // Método auxiliar para mudar de tela
    private void mudarTela(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) botaoEntrar.getScene().getWindow(); // Pega o Stage atual
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}