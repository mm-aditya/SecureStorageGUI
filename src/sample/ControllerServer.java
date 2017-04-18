package sample;

import com.jfoenix.controls.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import sample.SecStoreCORE.SecStore;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ControllerServer {

	//screen elements
    public Label label12;
    public Button button1;
    public Label textAr;
    public JFXButton viewfile;
    public VBox boxer;

    public ScrollPane scroller;


    public void startServerr() {
        button1.setDisable(true);
        SecStore server = new SecStore(6789, 5,this);
        new Thread(server).start();
        boxer = new VBox();
        viewfile = (JFXButton) Server.mainScene.lookup("#viewfile");
        scroller = (ScrollPane) Server.mainScene.lookup("#scroller");
        scroller.setContent(boxer);
    }

    public void modLab(int clientCount){

        label12.setText("Number of connected clients: " + clientCount);
    }

    public void setTextPane(String text){
//        textAr.setWrapText(true);
//        textAr.setText(text);
    }

    public void viewFILE() throws IOException{
        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(VolatileSR.fileLoc));
    }

    public void addToTree(String filename, long clientid,String filelocation){
        HBox temp = new HBox();
        JFXButton button = new JFXButton(filename);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.open(new File(filelocation));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Label newLab = new Label("Client "+clientid);
        button.setStyle("-fx-background-color:#fcfcfc");
        button.setButtonType(JFXButton.ButtonType.RAISED);
        temp.setMargin(newLab, new Insets(14.3,50,10,10));
        temp.setMargin(button, new Insets(10,5,10,50));
        temp.getChildren().addAll(newLab,button);
        boxer.getChildren().add(temp);
    }

    public void enableViewer(){
        viewfile.setDisable(false);
    }


}
