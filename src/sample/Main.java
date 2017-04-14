package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    static Stage window;
    static Parent root;
    static Parent root2;



    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root2 = FXMLLoader.load(getClass().getResource("sample02.fxml"));
        window.setTitle("Connect to server");
        window.setScene(new Scene(root));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToTwo() throws IOException {
        window.setTitle("File Upload");
        window.setScene(new Scene(root2));
        window.show();
    }

}
