package app;

import app.db.Repositorio;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega a primeira tela (Login)
            Parent root = FXMLLoader.load(getClass().getResource("/app/view/LoginView.fxml"));

            Scene scene = new Scene(root);

            // --- LINHA IMPORTANTE: CARREGA O CSS ---
            String css = getClass().getResource("/app/view/styles.css").toExternalForm();
            scene.getStylesheets().add(css);
            // ---------------------------------------

            primaryStage.setTitle("Loja Virtual POO (Sem DB)");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML da tela de Login:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Repositorio.init(); // Inicia banco falso
        launch(args);
    }
}