package com.jc.demo.model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ImageToBase64Test {
    public static void main(String[] args) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/static/uploads/product_img/sample.jpg"));
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        System.out.println(base64); // copy-paste this into your product's photoBase64 field
    }
}

