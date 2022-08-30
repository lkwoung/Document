package com.lkwoung.imageserverjson.demo;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    public JSONObject imageStore(MultipartFile image) {
        JSONObject result = new JSONObject();
        File file = new File("C:\\Users\\Desktop");

        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\Desktop\\test.jpg");
            byte data[] = image.getBytes();
            InputStream inputStream = image.getInputStream();

            int readCnt = 0;
            while((readCnt = inputStream.read(data)) != -1){
                fos.write(data, 0, readCnt);
            }
            fos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.put("state", 0);
        result.put("message", "Success");

        return result;
    }

}
