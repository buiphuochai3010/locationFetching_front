package netappui.cipherlock;

import javax.crypto.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static netappui.MainFrame.ANSI_RED;
import static netappui.MainFrame.ANSI_RESET;

public class RSACipher {
    private static PublicKey publicKey;

    public RSACipher() {
    }

    public static byte[] encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data.getBytes());
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            String error = e.toString();
            System.out.println(ANSI_RED + "Status: 500" +
                    "\nMessage: " + error + ANSI_RESET);
            return null;
        }
    }

    public static PublicKey getPublicKey() {
        return publicKey;
    }
    
    public static void setPublicKey(String base64PublicKey) {
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            String error = e.toString();
            System.out.println(ANSI_RED + "Status: 500" +
                    "\nMessage: " + error + ANSI_RESET);
        }
    }
}
