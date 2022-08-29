package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class TcpIpMultichatServer {
    HashMap clients;

    TcpIpMultichatServer() {
        clients = new HashMap();
        Collections.synchronizedMap(clients);
    }

    public static void main(String[] args) {
        new TcpIpMultichatServer().start();
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(7777);
            System.out.println(getTime() + " Server Is Ready To Connect");

            while (true) {
                socket = serverSocket.accept();
                System.out.println(getTime() + " A Connection Request From " + socket.getInetAddress());
                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerReceiver extends Thread{
        Socket socket;
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;

        ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }

        public void run() {
            String name = "";
            try {
                name = dataInputStream.readUTF();
                sendToAll("# " + name + " Is Just Arrive Here");

                clients.put(name, dataOutputStream);
                System.out.println("Now, Number Of Connectors is " + clients.size());

                while (dataInputStream != null) {
                    sendToAll(dataInputStream.readUTF());
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }finally {
                sendToAll("# " + name + " Left");
                clients.remove(name);
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]");
                System.out.println("Now, Number Of Connectors is " + clients.size());
            }//try
        }//run
    }//ReceiverThread

    void sendToAll(String msg) {
        Iterator iterator = clients.keySet().iterator();

        while (iterator.hasNext()) {
            try {
                DataOutputStream dataOutputStream = (DataOutputStream) clients.get(iterator.next());
                dataOutputStream.writeUTF(msg);
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }

    static String getTime() {
        String name = Thread.currentThread().getName();
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date()) + name;
    }
}
