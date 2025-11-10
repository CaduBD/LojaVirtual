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
            // O caminho /app/view/ indica para o JavaFX procurar na pasta 'view'
            Parent root = FXMLLoader.load(getClass().getResource("/app/view/LoginView.fxml"));

            Scene scene = new Scene(root);

            primaryStage.setTitle("Loja Virtual");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML da tela de Login:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 1. Inicializa o banco de dados falso com dados
        Repositorio.init();

        // 2. Lança a aplicação JavaFX
        launch(args);
    }
}