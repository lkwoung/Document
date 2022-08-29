package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TcpIpServer5 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = null;
            Socket socket = null;

            serverSocket = new ServerSocket(7777);
            System.out.println(getTime() + " Server Is Ready To Connect");

            socket = serverSocket.accept();

            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);

            sender.start();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // main

    static String getTime() {
        String name = Thread.currentThread().getName();
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date()) + name;
    } // getTime
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
