package com.lkwoung.imagesender;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Delivery {

    public static void write(OutputStream outputStream, Bitmap imageBitmap) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

        outputStream.write(size);

        System.out.println("byteArrayOutputStream.size() = " + byteArrayOutputStream.size());
        for (int i = 0; i < byteArrayOutputStream.size(); i += 20000) {
            if (byteArrayOutputStream.size() - i < 20000) {
                outputStream.write(byteArrayOutputStream.toByteArray(), i, byteArrayOutputStream.size()-i);
                outputStream.flush();
                System.out.println("send size = " + i);
                break;
            }
            outputStream.write(byteArrayOutputStream.toByteArray(), i, 20000);
            System.out.println("send size = " + i);
            Thread.sleep(1000);
        }

        System.out.println("Flushed: " + System.currentTimeMillis());
        Thread.sleep(10000);
        System.out.println("Closing: " + System.currentTimeMillis());

    } // write

    public static Bitmap read(InputStream inputStream) throws IOException {
        byte[] sizeArr = new byte[4];
        inputStream.read(sizeArr);

        int size = ByteBuffer.wrap(sizeArr).asIntBuffer().get();
        System.out.println("size = " + size);

        byte[] imageArr = new byte[size];

        int result = 0;
        for (int i = 0; i < size; i += 20000) {
            if ((size - i) < 20000) {
                result = inputStream.read(imageArr, i, size - i);
                System.out.println("result = " + result);
                break;
            }
            result = inputStream.read(imageArr, i, 20000);
            System.out.println("result = " + result);
        }
        ByteArrayInputStream img = new ByteArrayInputStream(imageArr);
        Bitmap imgBitmap = BitmapFactory.decodeStream(img);
        return imgBitmap;
    } // read
}
