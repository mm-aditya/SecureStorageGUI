package ProgrammingAssignment2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by HanWei on 10/4/2017.
 */
public class CheckValidity {
    public static void main(String[] args) {
        try {
            InputStream fis = new FileInputStream("src\\CSE\\ProgrammingAssignment2\\CA.crt");
            InputStream myFile = new FileInputStream("src\\CSE\\ProgrammingAssignment2\\1001522.crt");
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate CAcert =(X509Certificate)cf.generateCertificate(fis);
            X509Certificate myCert = (X509Certificate) cf.generateCertificate(myFile);

            PublicKey key = CAcert.getPublicKey();

//            CAcert.checkValidity();
//            CAcert.verify(key);

            myCert.checkValidity();
            myCert.verify(key);
        } catch (FileNotFoundException fNFE) {
            System.out.println("CA.crt not found.");
        } catch (CertificateException CE) {
            System.out.println(CE.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
