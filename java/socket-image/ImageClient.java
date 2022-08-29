package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ImageClient {
    public static void main(String[] args) throws Exception {
        // 서버와 연결하는 소켓 생성
        Socket socket = new Socket("192.168.0.65", 7777);

        // 소켓 OutputStream 생성 -> 클라이언트의 InputStream 과 연결되어서 통신하기 위함
        OutputStream outputStream = socket.getOutputStream();

        BufferedImage image = ImageIO.read(new File("C:\\java.jpg"));

        // 이미지 버퍼에 담긴 정보를 byte 형식으로 변환하기 위한 임시 바이트 배열
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 주어진 포멧에 맞게 OutputStream 에 써준다.
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        // 4바이트 할당, 크기 인트로 변환해서 받음
        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        // 이미지 파일의 크기를 전송
        outputStream.write(size);

        // byte 타입으로 변환하여 전송함.
        System.out.println("byteArrayOutputStream.size() = " + byteArrayOutputStream.size());
        outputStream.write(byteArrayOutputStream.toByteArray());
        Thread.sleep(1000);

        // 버퍼에 남아있는 내용 전송.
        outputStream.flush();

        // 입출력이 끝나기 전에 프로그램이 종료되는 것을 막으려고 쓴 코드 같으나, 입출력은 프로그램이 종료된 뒤에도 계속 작업을 마무리하고 종료되는걸로 알고 있음.
        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(1000);
        System.out.println("Closing: " + System.currentTimeMillis());

        // Closing this socket will also close the socket's InputStream and OutputStream.
        socket.close();
    }
}
