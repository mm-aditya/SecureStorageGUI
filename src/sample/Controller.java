package sample;

import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import sample.Secstorr.Client;

import java.io.IOException;

public class Controller {


    public ProgressBar bar;
    Thread a;
    Client client;

    public void moveToUpload() throws IOException {
        client = new Client(6789, this);
        a = new Thread(client);
        a.start();
        Main.switchToTwo();
    }

    public void startUpload(){
        VolatileCl.uploadReady=true;
        bar.setProgress(-1);
    }

    public void finUpload(){
        bar.setProgress(0.9F);
    }

}
