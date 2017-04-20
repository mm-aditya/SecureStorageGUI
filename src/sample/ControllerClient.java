package sample;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class ControllerClient {

    @FXML
    public ProgressBar bar;
    public JFXButton uploadB;
    public JFXTextField serverIP;
    public JFXComboBox serverIPBOX;
    public JFXTextField uploadfileName;
    public JFXTextField savefileName;
    @FXML
    public StackPane stackPane;
    @FXML
    public StackPane stackPaneMain;
    public Label currentProcess;
    public JFXRadioButton rad1;
    public JFXRadioButton rad2;

    Thread a;
    sample.SecStoreCORE.Client client;

    public String whatugot;

    public void moveToUpload() throws IOException {
        serverIPBOX = (JFXComboBox) Client.mainScene.lookup("#serverIPBOX");
        whatugot = serverIPBOX.getValue().toString();

        if(whatugot.equals(""))
            loadDialog();
        else {
            client = new sample.SecStoreCORE.Client(whatugot,6789, this);
            new Thread(client).start();
            Client.switchToTwo();
            bar = (ProgressBar) Client.mainScene.lookup("#bar");
        }
        uploadB = (JFXButton) Client.mainScene.lookup("#uploadB");
        uploadB.setDisable(true);
        currentProcess = (Label) Client.mainScene.lookup("#currentProcess");
        uploadfileName = (JFXTextField) Client.mainScene.lookup("#uploadfileName");
        savefileName = (JFXTextField) Client.mainScene.lookup("#savefileName");
        rad1 = (JFXRadioButton) Client.mainScene.lookup("#rad1");
        rad2 = (JFXRadioButton) Client.mainScene.lookup("#rad2");

        stackPaneMain = (StackPane) Client.mainScene.lookup("#stackPaneMain");
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
    }

    public void progressBarUploading(){
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

    public void loadDialogMain(String header, String message){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(header));
        content.setBody(new Text(message));
        JFXDialog dialog = new JFXDialog(stackPaneMain, content, JFXDialog.DialogTransition.CENTER );
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
