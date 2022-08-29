package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpIpServer2 {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println(getTime() + "Server Is Ready To Connect");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                System.out.println(getTime() + "Waiting For Connection.....");
                Socket socket = serverSocket.accept();

                // getIntAddress() Returns the address to which the socket is connected.
                System.out.println(getTime() + " A Connection Request From " + socket.getInetAddress());

                // Returns the remote port number to which this socket is connected
                System.out.println("Client Port / getPort() : " + socket.getPort());
                
                // Returns the local port number to which this socket is bound.
                System.out.println("Server Port / getLocalPort() : " + socket.getLocalPort());

                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                dataOutputStream.writeUTF("[NOTICE] Test Message from Server");
                System.out.println(getTime() + "Successfully Sent The Message.");

                dataOutputStream.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
