package netappui.cipherlock;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static netappui.MainFrame.ANSI_RED;
import static netappui.MainFrame.ANSI_RESET;

public class AESCipher {
    private SecretKey secretKey;

    public AESCipher() {
    }

    public SecretKey keyGen() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey key = keyGenerator.generateKey();
            //Encode Secret Key to byte array
            byte[] rawData = key.getEncoded();
            //Use Base64 to encode array to raw Base64 String
            String encodedKey = Base64.getEncoder().encodeToString(rawData);
            secretKey = new SecretKeySpec(rawData, 0, rawData.length, "AES");
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            String error = e.toString();
            System.out.println(ANSI_RED + "Status: 500" +
                    "Message: Somethings went wrong while generate AES key: " + error + ANSI_RESET);
            return null;
        }
    }

    public String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());

            // Encode dữ liệu đã mã hóa thành chuỗi Base64
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decode chuỗi Base64 để có mảng byte
            byte[] encryptedBytes = Base64.getDecoder().decode(input);

            // Giải mã dữ liệu
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Chuyển mảng byte giải mã thành chuỗi
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
