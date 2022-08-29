package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class TcpIpClient5 {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        Socket socket = null;
        try {
            socket = new Socket(serverIP, 7777);
            System.out.println("Connecting With Server / Server IP :" + serverIP);
            System.out.println("Successfully Connect To Server");

            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);

            sender.start();
            receiver.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // main
}
class Sender extends Thread {
    Socket socket;
    DataOutputStream dataOutputStream;
    String name;

    Sender(Socket socket) {
        this.socket = socket;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            name = "[" + socket.getInetAddress() + ":" + socket.getPort() + "] ";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (dataOutputStream != null) {
            try {
                dataOutputStream.writeUTF(name + scanner.nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} // Sender


class Receiver extends Thread{
    Socket socket;
    DataInputStream dataInputStream;

    Receiver(Socket socket) {
        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (dataInputStream != null) {
            try {
                System.out.println(dataInputStream.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} // Receiver