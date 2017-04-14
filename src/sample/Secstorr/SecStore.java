package sample.Secstorr;

import javafx.application.Platform;
import javafx.concurrent.Task;
import sample.ControllerServer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by HanWei on 11/4/2017.
 */

// when connected, do the handshake protocol
// Wait for file to be uploaded.
// Save file.
// Time the transfer duration (including encrypting and decrypting)
// Usable interface. For example, you may develop a dynamic GUI that shows the changing states of the
// various modules as your “protocol” proceeds over time.
// Monitoring of transfers or diagnosis of transfer problems (e.g., traceroute between client and server).
// Support for anycast upload to distributed servers.
// SSH for security.

public class SecStore extends Task {
    private ServerSocket server;
    private final Executor exec;
    private final int portNum;
    private X509Certificate serverCert;
    private PrivateKey privateKey;
    ControllerServer receivCont;

    public SecStore(int port, int numThreads, ControllerServer sent) {
        portNum = port;
        exec = Executors.newFixedThreadPool(numThreads);
        receivCont = sent;
        try {
            privateKey = getPrivateKey("src\\sample\\Secstorr\\privateServer.der");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            serverCert = (X509Certificate) cf.generateCertificate(new FileInputStream("src\\sample\\Secstorr\\1001522.crt"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            server = new ServerSocket(portNum);
            while (true) {
                final Socket connection = server.accept();
                Runnable task = () -> {
                    try {
                        handleRequest(connection);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                };
                exec.execute(task);
            }
        } catch (IOException ioE) {
            System.out.println(ioE.getMessage());
            ioE.printStackTrace();
        }
    }

    private void handleRequest(Socket socketConnection) throws Exception {
        OutputStream out = socketConnection.getOutputStream();
        InputStream in = socketConnection.getInputStream();
        System.out.println("Connection established");
        Platform.runLater(new Runnable() {
            @Override public void run() {
                receivCont.modLab();
            }
        });
//        PrintWriter printer = new PrintWriter(out);
        BufferedReader buff = new BufferedReader(new InputStreamReader(in));
        String inLine = buff.readLine();
        out.write(encryptBytes(inLine.getBytes(), "RSA/ECB/PKCS1Padding", privateKey));
        inLine = buff.readLine();
        if (inLine.equals("Cert pls")) {
            out.write(serverCert.getEncoded());
            out.flush();
        }
        inLine = buff.readLine();
        if (inLine.equals("OK CAN")) {
            // TODO: send a sym key then wait (CP2)
            socketConnection.setSoTimeout(10000);   // TODO: clean up sockets and maybe reset timeout
            receiveFile(in, "RSA/ECB/PKCS1Padding", privateKey);
        }
    }

    private void receiveFile(InputStream inputStream, String decryptType, Key key) throws Exception {
        System.out.println("BOOYAH");
        String message = new String(decryptBytes(readAll(inputStream), decryptType, key));
        System.out.println(message); // not the right message. Aditya pls halp.
        Platform.runLater(new Runnable() {
            @Override public void run() {
                receivCont.setTextPane(message);
            }
        });
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

    private PrivateKey getPrivateKey(String location) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(location).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private byte[] encryptBytes(byte[] toBeEncrypted, String encryptType, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(toBeEncrypted);
    }

    private byte[] decryptBytes(byte[] toBeDecrypted, String encryptType, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(Cipher.DECRYPT_MODE, key);
//        return rsaCipher.doFinal(toBeDecrypted);
        return blockCipher(toBeDecrypted, Cipher.DECRYPT_MODE, cipher);
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

    @Override
    protected Object call() throws Exception {
        startServer();
        return null;
    }
}
