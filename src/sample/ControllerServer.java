package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import sample.Secstorr.SecStore;

import java.io.IOException;

public class ControllerServer {

	//screen elements
    public Label label12;
    public Button button1;
    public Label textAr;

    public void startServerr() {
        button1.setDisable(true);
        SecStore server = new SecStore(6789, 5,this);
        new Thread(server).start();
    }

    public void modLab(){
        label12.setText("Connection Established");
    }

    public void setTextPane(String text){
        textAr.setWrapText(true);
        textAr.setText(text);
    }

}
