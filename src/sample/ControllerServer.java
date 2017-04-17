package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import sample.SecStoreCORE.SecStore;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ControllerServer {

	//screen elements
    public Label label12;
    public Button button1;
    public Label textAr;
    public JFXButton viewfile;

    public void startServerr() {
        button1.setDisable(true);
        SecStore server = new SecStore(6789, 5,this);
        new Thread(server).start();
        viewfile = (JFXButton) Server.mainScene.lookup("#viewfile");
    }

    public void modLab(){
        label12.setText("Connection Established");
    }

    public void setTextPane(String text){
//        textAr.setWrapText(true);
//        textAr.setText(text);
    }

    public void viewFILE() throws IOException{
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(VolatileSR.fileLoc));
    }

    public void enableViewer(){
        viewfile.setDisable(false);
    }


}
