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

    @FXML
    protected void handleEntrar() {
        Cliente cliente = Repositorio.getClientePadrao();

        if (cliente != null) {
            SessaoManager.getInstance().setClienteLogado(cliente);
            mudarTela("CatalogoView.fxml");
        } else {
            System.err.println("Nenhum cliente padrão encontrado!");
        }
    }

    private void mudarTela(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) botaoEntrar.getScene().getWindow();
            Scene scene = new Scene(root);

            // REAPLICA O CSS NA NOVA CENA
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}