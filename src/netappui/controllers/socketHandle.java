package netappui.controllers;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import netappui.cipherlock.AESCipher;

public class socketHandle {
    private static AESCipher aes;
    private static Socket socket;
    private static BufferedReader inStream;
    private static BufferedWriter outStream;
    private static BufferedReader stdIn;

    public socketHandle(AESCipher aes, Socket socket, BufferedReader inStream, BufferedWriter outStream, BufferedReader stdIn) {
        this.aes = aes;
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
        this.stdIn = stdIn;
    }

    public static void closeSocket() throws IOException {
        stdIn.close();
        inStream.close();
        outStream.close();
        socket.close();
        System.out.println("[Client] Client is closed");
    }

    public static void sendData(String input) throws IOException {
        //Encrypt input
        input = aes.encrypt(input);
        outStream.write(input + "\n");
        outStream.flush();
    }

    public static String receiveData() throws JSONException, IOException {
        String res = inStream.readLine();
        System.out.println("[Client] Cipher response: " + res);
        res = aes.decrypt(res);
        if (!res.isBlank()) {
            //Decrypt response
            JSONObject obj = new JSONObject(res);
            System.out.println("[Client] Decrypted response:");
            System.out.println(obj.toString(1));
            return obj.toString();
        } else {
            return null;
        }
    }
}
