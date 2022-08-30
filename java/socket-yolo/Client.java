package socket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    public static void main(String[] args) throws Exception {
        // Create Socket And The Others
        Socket socket = new Socket("127.0.0.1", 7777);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        // Read Image From Local And Send The Image To Server
        BufferedImage image = ImageIO.read(new File("C:\\java.jpg"));
        Delivery.write(outputStream, image);

        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());

        // Read Image Processed By Yolo From Server
        BufferedImage yoloImage = Delivery.read(inputStream);

        // Store The Yolo Image At Local
        System.out.println("Received " + yoloImage.getHeight() + "x" + yoloImage.getWidth() + ": " + getTime());
        ImageIO.write(yoloImage, "jpg", new File("C:\\Temp\\clientjava.jpg"));

        // Close
        socket.close();
        System.out.println(getTime() + " finish");
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
