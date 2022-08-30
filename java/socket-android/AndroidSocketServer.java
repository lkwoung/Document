package androidServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AndroidSocketServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7777);
            System.out.println(getTime() + " Server Is Ready To Connect");
            while (true) {
                System.out.println(getTime() + "Waiting For Connection.....");
                Socket socket = serverSocket.accept();
                System.out.println(getTime() + " A Connection Request From " + socket.getInetAddress());

                InetAddress clientHost = socket.getLocalAddress();
                int clientPort = socket.getPort();
                System.out.println("Host : " + clientHost + "Client : " + clientPort);

                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                System.out.println(dataInputStream.readUTF());

                dataInputStream.close();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
