package sample;

import com.jfoenix.controls.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import sample.SecStoreCORE.Client;

import javax.swing.*;
import java.io.IOException;

public class Controller {

    final SimpleDoubleProperty prop = new SimpleDoubleProperty(0);

    @FXML
    public ProgressBar bar;
    public JFXButton uploadB;
    public JFXTextField serverIP;
    public JFXComboBox serverIPBOX;
    public JFXTextField uploadfileName;
    public JFXTextField savefileName;
    public StackPane stackPane;
    public Label currentProcess;
    public JFXRadioButton rad1;
    public JFXRadioButton rad2;

    Thread a;
    Client client;

    public String whatugot;

    public void moveToUpload() throws IOException {
        //serverIP = (JFXTextField) Main.mainScene.lookup("#serverIP");
        serverIPBOX = (JFXComboBox) Main.mainScene.lookup("#serverIPBOX");
        whatugot = serverIPBOX.getValue().toString();

        if(whatugot.equals(""))
            loadDialog();
        else {
            client = new Client(whatugot,6789, this);
            new Thread(client).start();
            Main.switchToTwo();
            bar = (ProgressBar) Main.mainScene.lookup("#bar");
        }
        uploadB = (JFXButton) Main.mainScene.lookup("#uploadB");
        uploadB.setDisable(true);
        currentProcess = (Label) Main.mainScene.lookup("#currentProcess");
        uploadfileName = (JFXTextField) Main.mainScene.lookup("#uploadfileName");
        savefileName = (JFXTextField) Main.mainScene.lookup("#savefileName");
        rad1 = (JFXRadioButton) Main.mainScene.lookup("#rad1");
        rad2 = (JFXRadioButton) Main.mainScene.lookup("#rad2");
    }

    public void startUpload(){
        VolatileCl.fileToUpload = uploadfileName.getText();
        VolatileCl.receivedFileName = savefileName.getText();
        if (rad1.isSelected())
            VolatileCl.encryptiontype=1;
        else if (rad2.isSelected())
            VolatileCl.encryptiontype=2;
        else
            VolatileCl.encryptiontype=2;

        VolatileCl.uploadReady=true;
        bar.setProgress(-1);

    }

    public void finUpload(){
        bar.setProgress(1);
    }

    public void enableUpload(){
        uploadB.setDisable(false);
    }


    public void loadDialog(){

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("NO SERVER IP"));
        content.setBody(new Text("Please enter a valid server ip that you want to connect\nand transfer your files to"));
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER );
        JFXButton button = new JFXButton("OK");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }

    public void setProcess(String message){
        currentProcess.setText(message);
    }

}
