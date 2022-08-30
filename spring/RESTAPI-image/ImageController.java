package com.lkwoung.imageserverjson.demo;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @PostMapping("/image")
    public JSONObject requestImage(@RequestParam MultipartFile image) {
        return imageService.imageStore(image);
    }

    @GetMapping("/test")
    public String test() {
        return "서버 테스트 성공";
    }

}

