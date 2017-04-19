package sample.SecStoreCORE;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.Controller;
import sample.VolatileCl;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by HanWei on 11/4/2017.
 */

public class Client extends Task{
    int portNumber;
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private X509Certificate serverCert;
    private X509Certificate CACert;
    private SecretKey symKey;

    private int encryptionType;
    private String sendingFile;
    private String receivingFile;
    private boolean trust;
    private Controller contextController;

    public void startHere() {
        //Client client = new Client("localhost", 6789);
        try {
            while(VolatileCl.appRunning) {
                if (!trust)
                    this.handshake();

                else {

                    while ((!VolatileCl.uploadReady) && (VolatileCl.appRunning)) {
//                        System.out.println("Name of input: " + VolatileCl.fileToUpload);
//                        System.out.println("Name of output: " + VolatileCl.receivedFileName + "\n");
                        ;
                    }

                    if(VolatileCl.appRunning) {
                        sendingFile = VolatileCl.fileToUpload;
                        receivingFile = VolatileCl.receivedFileName;
                        encryptionType = VolatileCl.encryptiontype;

                        System.out.println("Does file exits?: " + new File("src" + File.separator + "sample" + File.separator + "SecStoreCORE" + File.separator + "sampleData" + File.separator + sendingFile).exists());

                        if (new File("src" + File.separator + "sample" + File.separator + "SecStoreCORE" + File.separator + "sampleData" + File.separator + sendingFile).exists()) {
//
//                        if (new File("src" + File.separator + "sample" + File.separator + "outputs" + File.separator + receivingFile).exists()){
//                            Platform.runLater(new Runnable() {
//                                @Override public void run() {
//                                    contextController.loadDialogMain("WARNING: File exists!","This file already exists in the\noutput directory! This is just a warning.");
//                                }
//                            });
//                        }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    contextController.progressBarUploading();
                                }
                            });

                            int numTrial = 1;
                            if (receivingFile.equals(""))
                                receivingFile = "untitled.txt";
                            switch (encryptionType) {
                                case 1:
                                    System.out.println("RSA");
                                    this.testEncryption(numTrial, "RSA", "src" + File.separator + "sample" + File.separator + "SecStoreCORE" + File.separator + "sampleData" + File.separator + "" + sendingFile, receivingFile);
                                    break;
                                case 2:
                                    System.out.println("AES");
                                    this.testEncryption(numTrial, "AES", "src" + File.separator + "sample" + File.separator + "SecStoreCORE" + File.separator + "sampleData" + File.separator + "" + sendingFile, receivingFile);
                                    break;
                                default:
                                    break;
                            }
                            System.out.println("Finish");
                            VolatileCl.uploadReady = false;
                        } else {
                            VolatileCl.uploadReady = false;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    contextController.loadDialogMain("Input file does not exist!", "This file does not exist in the\nstandard input directory.\nDir: src\\sample\\SecStoreSAMPLE\\sampleData");
                                }
                            });
                        }
                    }
                }
            }

//            System.out.println("Pure RSA: small");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "smallFile.txt", "smallRSA.txt");
//            System.out.println("Pure RSA: medium");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "medianFile.txt", "mediumRSA.txt");
//            System.out.println("Pure RSA: large");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "largeFile.txt", "largeRSA.txt");
//            System.out.println("RSA + AES: small");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "smallFile.txt", "smallAES.txt");
//            System.out.println("RSA + AES: medium");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "medianFile.txt", "mediumAES.txt");
//            System.out.println("RSA + AES: large");
//            client.testEncryption(numTrial, "RSA", "src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "largeFile.txt", "largeAES.txt");
//            client.uploadFile("src" + File.separator + "ProgrammingAssignment2" + File.separator + "sampleData" + File.separator + "smallFile.txt", "meow.txt", "AES/ECB/PKCS5Padding");
//            System.out.println("Ok all done.");

            Thread.currentThread().interrupt();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public Client(String ipAddress, int portNum, Controller controller) {

        portNumber = portNum;
        socket = new Socket();
        trust = false;
        contextController = controller;

        SocketAddress sockaddr = new InetSocketAddress(ipAddress, portNumber);    // set this to IP address of server
        try {
            socket.connect(sockaddr);
            socket.setSoTimeout(1000);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            CACert = (X509Certificate) cf.generateCertificate(new FileInputStream("src" + File.separator + "sample" + File.separator + "SecStoreCORE" + File.separator + "CA.crt"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void handshake() throws Exception {

        try {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    contextController.setProcess("Generating nonce...");
                }
            });


            String cNonce = generateCnonce();
            out.write(cNonce.getBytes());
            byte[] encryptedCnonce = waitForResponse(in);
            out.write("Cert pls".getBytes());
            System.out.println("Asking for cert");


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    contextController.setProcess("Obtaining X509 Certificate...");
                }
            });


            byte[] byteCert = waitForResponse(in);
            serverCert = getCert(byteCert);
            if (verifyServer(cNonce, encryptedCnonce, serverCert.getPublicKey())) {
                out.write("OK CAN".getBytes());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        contextController.setProcess("SecStore Validated!");
                        contextController.enableUpload();
                    }
                });

                trust = true;
                byte[] byteSecretKey = decryptBytes(readAll(in), "RSA/ECB/PKCS1Padding", serverCert.getPublicKey());
                getSymKey(byteSecretKey);
            }
        }catch (Exception e){
            Thread.currentThread().interrupt();
        }
    }

    private boolean verifyServer(String cNonce, byte[] encryptedCnonce, Key key) throws Exception {
        return cNonce.equals(new String(decryptBytes(encryptedCnonce, "RSA/ECB/PKCS1Padding", key)));
    }

    private X509Certificate getCert(byte[] byteCert) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream stream = new ByteArrayInputStream(byteCert);
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(stream);
        stream.close();
        verifyCert(certificate);
        return certificate;
    }

    private void verifyCert(X509Certificate certificate) throws Exception {
        certificate.checkValidity();
        certificate.verify(CACert.getPublicKey());
    }

    private byte[] readAll(InputStream in) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while (true) {
            try {
                nRead = in.read(data, 0, data.length);
                buffer.write(data, 0, nRead);
            } catch (SocketTimeoutException sTimeout) {
                break;
            }
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    private byte[] encryptBytes(byte[] toBeEncrypted, String encryptType, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        if (encryptType.contains("AES")) return cipher.doFinal(toBeEncrypted);
        return blockCipher(toBeEncrypted, Cipher.ENCRYPT_MODE, cipher);
    }

    private byte[] decryptBytes(byte[] toBeDecrypted, String decryptType, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(decryptType);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return blockCipher(toBeDecrypted, Cipher.DECRYPT_MODE, cipher);
    }

    private void uploadFile(String pathToFile, String name, String encryptionType) throws Exception {
        Key key;
        if (encryptionType.contains("RSA")) key = serverCert.getPublicKey();
        else key = symKey;

        Platform.runLater(new Runnable() {
            @Override public void run() {
                contextController.setProcess("Reading file...");
            }
        });

        File upload = new File(pathToFile);

        Platform.runLater(new Runnable() {
            @Override public void run() {
                contextController.setProcess("Encrypting with " + (encryptionType.contains("RSA" )? "RSA": "AES") +" ...");
            }
        });

        byte[] toSend = encryptBytes(Files.readAllBytes(upload.toPath()), encryptionType, key);
        System.out.println("Size: " + toSend.length);
        out.write(encryptionType.substring(0, 3).getBytes());
        Platform.runLater(new Runnable() {
            @Override public void run() {
                contextController.setProcess("Decrypting server side...");
            }
        });
        waitForResponse(in);
        out.write((name).getBytes());
        waitForResponse(in);
        out.write(toSend);
        out.flush();
        waitForServer();

        Platform.runLater(new Runnable() {
            @Override public void run() {
                contextController.setProcess("Done!");
            }
        });
    }

    private byte[] waitForResponse(InputStream in) throws Exception {
        byte[] data = new byte[0];
        while (data.length == 0) {
            data = readAll(in);
        }
        return data;
    }

    private void waitForServer() throws Exception {
        String line = new String(waitForResponse(in));
        if (line.equals("Done!")) System.out.println("Received done");
        else System.out.println("Cannot receive Done");
    }

    private void getSymKey(byte[] encodedKey) throws NoSuchAlgorithmException {
        symKey = new SecretKeySpec(encodedKey, "AES");
    }

    private String generateCnonce() {
        return new BigInteger(50, new SecureRandom()).toString();
    }

    public void closeConnection() throws Exception {
        in.close();
        out.close();
        socket.close();
    }

    private byte[] blockCipher(byte[] bytes, int mode, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 117 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
        int length = (mode == Cipher.ENCRYPT_MODE) ? 117 : 128;

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i = 0; i < bytes.length; i++) {

            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)) {
                //execute the operation
                scrambled = cipher.doFinal(buffer);
                // add the result to our total result.
                toReturn = append(toReturn, scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i % length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = cipher.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }

    private void testEncryption(int numTrial, String RSAAES, String path, String fileName) throws Exception {
        long start = System.currentTimeMillis();
        String encryption;
        System.out.println("Type of encrypt: " + RSAAES);
        if (RSAAES.equals("RSA")) encryption = "RSA/ECB/PKCS1Padding";
        else if (RSAAES.equals("AES")) encryption = "AES/ECB/PKCS5Padding";
        else return;

        FileWriter writer = new FileWriter("src" + File.separator + "sample" + File.separator + "Timings.csv", true);
        writer.append(fileName + ",");
        long total = 0;
        long trialTiming;
        long startTrial;

        for (int i = 0; i < numTrial; i++) {
            startTrial = System.currentTimeMillis();
            uploadFile(path, fileName, encryption);
            trialTiming = System.currentTimeMillis() - startTrial;
            System.out.println(trialTiming);
            writer.append("" + trialTiming + ",");
            total += trialTiming;
        }
        long average = total / numTrial;
        Platform.runLater(new Runnable() {
            @Override public void run() {
                contextController.finUpload();
                contextController.setProcess("File encrypted and uploaded successfully! Time taken: " + average + "ms");
            }
        });
        writer.write("" + average + "\n");
        writer.flush();
        writer.close();
        System.out.println("Average time: " + average);
    }

    @Override
    protected Object call() throws Exception {
        startHere();
        return null;
    }
}
