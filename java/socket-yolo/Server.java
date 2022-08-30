package socket;

import detection.Detection;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server{
    public static void main(String[] args) throws Exception {
        // Create Socket And Others
        ServerSocket serverSocket = new ServerSocket(7777);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        System.out.println("Reading: " + getTime());

        // Receive Image From Client
        BufferedImage image = Delivery.read(inputStream);

        // Store The Image At Local
        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + getTime());
        ImageIO.write(image, "jpg", new File("C:\\Temp\\serverjava.jpg"));
        Thread.sleep(1000);

        // Yolo Process
        Detection dection = new Detection();
        image = dection.yolo(image);

        // Send The Image to Client
        Delivery.write(outputStream, image);

        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());

        // Close
        serverSocket.close();
        System.out.println(getTime() + " finish");
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
