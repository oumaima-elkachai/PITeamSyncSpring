package com.example.attendance.Backend.services.IMPL;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import com.example.attendance.Backend.services.interfaces.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceIMPL implements CloudinaryService {

    private final Cloudinary cloudinary; // Injecté depuis Spring config

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Erreur upload Cloudinary", e);
        }
    }
}