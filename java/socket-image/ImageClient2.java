package client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageClient2 {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("192.168.0.65", 7777);
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        BufferedImage image = ImageIO.read(new File("C:\\java.jpg"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        outputStream.write(size);

        System.out.println("byteArrayOutputStream.size() = " + byteArrayOutputStream.size());
        for (int i = 0; i < byteArrayOutputStream.size(); i += 20000) {
            if (byteArrayOutputStream.size() - i < 20000) {
                outputStream.write(byteArrayOutputStream.toByteArray(), i, byteArrayOutputStream.size()-i);
                outputStream.flush();
                break;
            }
            outputStream.write(byteArrayOutputStream.toByteArray(), i, 20000);
            Thread.sleep(100);
        }

        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());


        // receive the image
        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);
        int size1 = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        byte[] imageAr = new byte[size1];

        int result = 0;
        for (int i = 0; i < size1; i += 20000) {
            if((size1-i) < 20000){
                result = inputStream.read(imageAr, i, size1-i);
                System.out.println("result = " + result);
                break;
            }
            result = inputStream.read(imageAr, i, 20000);
            System.out.println("result = " + result);
        }

        BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(imageAr));

        System.out.println("Received " + image1.getHeight() + "x" + image1.getWidth() + ": " + getTime());
        ImageIO.write(image1, "jpg", new File("C:\\Temp\\clientjava.jpg"));

        socket.close();
        System.out.println(getTime() + " finish");
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
