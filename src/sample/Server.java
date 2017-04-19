package sample;/**
 * Created by aditya on 13/4/2017.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.SecStoreCORE.SecStore;

import java.io.IOException;

public class Server extends Application {


    static Stage window;
    static Parent root;
    static Parent root2;
    static Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        root = FXMLLoader.load(getClass().getResource("sampleServer.fxml"));
        window.setTitle("SecStore");
        mainScene = new Scene(root,500,500);
        window.setScene(mainScene);
        window.show();
    }

    @Override
    public void stop() throws Exception {
        VolatileSR.isRunning = false;
        SecStore.server.close();
    }
}
