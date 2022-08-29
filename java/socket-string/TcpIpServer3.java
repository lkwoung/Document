package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpIpServer3 {
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

                // If the timeout expires, a java.net.SocketTimeoutException is raised, though the ServerSocket is still valid.
                serverSocket.setSoTimeout(5 * 1000);

                Socket socket = serverSocket.accept();
                System.out.println(getTime() + " A Connection Request From " + socket.getInetAddress());

                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                dataOutputStream.writeUTF("[NOTICE] Test Message from Server");
                System.out.println(getTime() + "Successfully Sent The Message.");

                dataOutputStream.close();
                socket.close();
            } catch (SocketTimeoutException se) {
                System.out.println("Time Out >>> Server Has Been Terminated");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
