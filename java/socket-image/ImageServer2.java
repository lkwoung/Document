package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageServer2{
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(7777);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        System.out.println("Reading: " + getTime());

        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);

        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        System.out.println("size = " + size);

        byte[] imageAr = new byte[size];

        int result = 0;
        for (int i = 0; i < size; i += 20000) {
            if((size-i) < 20000){
                result = inputStream.read(imageAr, i, size-i);
                System.out.println("result = " + result);
                break;
            }
            result = inputStream.read(imageAr, i, 20000);
            System.out.println("result = " + result);
        }

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + getTime());
        ImageIO.write(image, "jpg", new File("C:\\Temp\\serverjava.jpg"));


        outputStream.write(sizeAr);

        for (int i = 0; i < imageAr.length; i += 20000) {
            if (imageAr.length - i < 20000) {
                outputStream.write(imageAr, i, imageAr.length-i);
                outputStream.flush();
                break;
            }
            outputStream.write(imageAr, i, 20000);
            Thread.sleep(100);
        } // refactoring


        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());


        serverSocket.close();
        System.out.println(getTime() + " finish");
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
