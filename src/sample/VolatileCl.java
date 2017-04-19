package sample;

import java.util.ArrayList;

/**
 * Created by Aditya on 15/4/17.
 */
public class VolatileCl {
    public static volatile boolean uploadReady = false;
    public static volatile String fileToUpload;
    public static volatile String receivedFileName;
    public static volatile int encryptiontype;
    public static volatile boolean appRunning = true;


    public static volatile ArrayList<ArrayList<String>> clientData;
    public static volatile ArrayList<ArrayList<String>> clientButton;
}
