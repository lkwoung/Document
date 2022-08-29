package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;

public class TcpIpClient {
    public static void main(String[] args) {
        try {
            String serverIP = "127.0.0.1";

            System.out.println("Connecting With Server / Server IP :" + serverIP);

            Socket socket = new Socket(serverIP, 7777);

            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            System.out.println("A Message From Server : " + dataInputStream.readUTF());
            System.out.println("Terminate The Connection");

            dataInputStream.close();
            socket.close();
            System.out.println("The Connection Has Been Terminated");
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
