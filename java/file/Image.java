package image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Image {
    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageIO.read(new File("C:\\java.jpg"));

        // 이미지 버퍼에 담긴 정보를 byte 형식으로 변환하기 위한 임시 바이트 배열
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 주어진 포멧에 맞게 OutputStream 에 써준다.
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        // 4바이트 할당, 크기 인트로 변환해서 받음
        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        ByteBuffer bb = (ByteBuffer) ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).rewind();
        System.out.println("byteArrayOutputStream = " + byteArrayOutputStream.size());
        System.out.println("getInt" + bb.getInt());

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        int sizeAr = ByteBuffer.wrap(size).asIntBuffer().get();
        byte[] imageAr = new byte[sizeAr];

        for (int i = 0; i < byteArray.length; i++) {
            imageAr[i] = byteArray[i];
        }

        // InputStream 으로 들어온 것을 디코딩하여 BufferedImage 로 반환해준다.
        BufferedImage imageBuf = ImageIO.read(new ByteArrayInputStream(imageAr));

        // 이미지에 대한 정보와 완료된 시간 표시
        System.out.println("Received " + imageBuf.getHeight() + "x" + imageBuf.getWidth() + ": " + getTime());

        // 이미지를 저장
        ImageIO.write(imageBuf, "jpg", new File("C:\\Temp\\java.jpg"));
    }
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
