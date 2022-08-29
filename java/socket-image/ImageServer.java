package server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageServer{
    public static void main(String[] args) throws Exception {
        // ServerSocket 을 열고 클라이언트의 요청을 기다린다.
        ServerSocket serverSocket = new ServerSocket(7777);
        // 클라이언트의 요청이 받아지면,
        Socket socket = serverSocket.accept();
        // 입력을 받기위해 InputStream 을 연다.
        InputStream inputStream = socket.getInputStream();

        // 읽기 시작한 시간
        System.out.println("Reading: " + getTime());

        // 이미지의 사이즈를 읽어온다. (int형 범위)
        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);

        // byte Array 를 Buffer 로 감싸준다. byte buffer 를 int buffer 로 반환해준다.
        // int buffer 에서 int 값을 읽어온다.
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        System.out.println("size = " + size);

        // 이미지를 읽어옴
        byte[] imageAr = new byte[size];
        int result = inputStream.read(imageAr);
        System.out.println("result = " + result);

        // InputStream 으로 들어온 것을 디코딩하여 BufferedImage 로 반환해준다.
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

        // 이미지에 대한 정보와 완료된 시간 표시
        System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": " + getTime());

        // 이미지를 저장
        ImageIO.write(image, "jpg", new File("C:\\Temp\\java.jpg"));

        // 소켓 닫기
        serverSocket.close();
    }

    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("{hh:mm:ss}");
        return f.format(new Date());
    }
}
