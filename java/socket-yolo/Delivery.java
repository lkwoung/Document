package socket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class Delivery {
    /**
     * send the byte-array to server/client.
     * @param inputStream
     * @throws IOException
     */
    public static BufferedImage read(InputStream inputStream) throws IOException {
        byte[] sizeArr = new byte[4];
        inputStream.read(sizeArr);

        int size = ByteBuffer.wrap(sizeArr).asIntBuffer().get();
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
        return image;
    }// read

    /**
     * receive the byte-array from server/client.
     * @param outputStream
     * @param image
     * @throws IOException
     * @throws InterruptedException
     */
    public static void write(OutputStream outputStream, BufferedImage image) throws IOException, InterruptedException {
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
            Thread.sleep(1000);
        }
    }// write
}
