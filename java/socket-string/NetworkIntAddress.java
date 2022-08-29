package network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class NetworkIntAddress {
    public static void main(String[] args) {
        InetAddress ip = null;
        InetAddress[] ipArr = null;

        try {
            ip = InetAddress.getByName("www.naver.com");
            System.out.println("getHostName() : " + ip.getHostName());
            System.out.println("getHostAddress() : " + ip.getHostAddress());
            System.out.println("toString() : " + ip.toString());

            System.out.println();
            byte[] ipAddr = ip.getAddress();
            System.out.println("getAddress() : " + Arrays.toString(ipAddr));

            System.out.println();
            String result = "";
            for (int i = 0; i < ipAddr.length; i++) {
                System.out.println(ipAddr[i]);
                result += (ipAddr[i] < 0) ? ipAddr[i] + 256 : ipAddr[i];
                result += ".";
            }
            System.out.println("getAddress()+256 : " + result);
            System.out.println(ipAddr.length);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            ip = InetAddress.getLocalHost();
            System.out.println();
            System.out.println("getHostName : " + ip.getHostAddress());
            System.out.println("getHostAddress : " + ip.getHostAddress());
            System.out.println();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            ipArr = InetAddress.getAllByName("www.naver.com");
            for (int i = 0; i < ipArr.length; i++) {
                System.out.println("ipArr[" + i + "] : " + ipArr[i]);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }
}
