package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpIpServer4 implements Runnable {
    ServerSocket serverSocket;
    Thread[] threadArr;

    public static void main(String[] args) {
        TcpIpServer4 server = new TcpIpServer4(Integer.parseInt(args[0]));
        server.start();
    }

    TcpIpServer4() {
        this(1);
    }

    TcpIpServer4(int num) {
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println(getTime() + " Server Is Ready To Connect");
            threadArr = new Thread[num];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        for (int i = 0; i < threadArr.length; i++) {
            threadArr[i] = new Thread(this);
            threadArr[i].start();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(getTime() + " Is Waiting For Connection Request");

                Socket socket = serverSocket.accept();
                System.out.println(getTime() + " A Connection Request From " + socket.getInetAddress());

                OutputStream outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                dataOutputStream.writeUTF("[NOTICE] Test Message from Server");
                System.out.println(getTime() + " Successfully Sent The Message.");

                dataOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static String getTime() {
        String name = Thread.currentThread().getName();
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date()) + name;
    }
}
