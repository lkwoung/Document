package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class TcpIpMultichatClient {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("USAGE : java TcpIpMultichatClient ChatName");
            System.exit(0);
        }
        String serverIp = "127.0.0.1";

        try {
            Socket socket = new Socket(serverIp, 7777);
            System.out.println("Connecting With Server / Server IP :" + serverIp);
            Thread sender = new Thread(new ClientSender(socket, args[0]));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ClientSender extends Thread {
        Socket socket;
        DataOutputStream dataOutputStream;
        String name;

        ClientSender(Socket socket, String name) {
            this.socket = socket;
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }

        public void run() {
            Scanner scanner = new Scanner(System.in);

            try {
                if (dataOutputStream != null)
                    dataOutputStream.writeUTF(name);

                while (true) {
                    dataOutputStream.writeUTF("["+name+"]"+scanner.nextLine());
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }//run
    }// ClientSender

    static class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream dataInputStream;

        ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }

        public void run() {
            while (dataInputStream != null) {
                try {
                    System.out.println(dataInputStream.readUTF());
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
        }//run
    }//ClientReceiver
}//class
