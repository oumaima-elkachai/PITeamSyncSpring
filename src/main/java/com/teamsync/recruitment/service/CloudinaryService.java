

package com.teamsync.recruitment.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;


    // Cloudinary credentials: Set these from your Cloudinary account
    public CloudinaryService() {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dm2gufdmn",
                "api_key", "745773944685639",
                "api_secret", "vAx35vcc91rIfxkHl8LcT9G3uBI"
        ));
    }

    public String uploadImage(MultipartFile file) throws IOException {
        // Upload the image to Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Log the full upload result for debugging
        System.out.println("Cloudinary Upload Result: " + uploadResult);

        // Return the URL of the uploaded image
        return (String) uploadResult.get("url");
    }


    /*public String uploadFile(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }*/


    public String uploadFile(MultipartFile file) throws IOException {
        // Validate PDF file type
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Extract the filename
        String originalFilename = file.getOriginalFilename();
        String filenameWithExtension = originalFilename != null ? originalFilename : "file.pdf"; // Default fallback

        // Upload file to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto",
                "type", "upload",
                "use_filename", true,               // Keep the original filename
                "unique_filename", true,            // Avoid filename conflicts
                "filename", filenameWithExtension   // Ensure extension is kept
        ));

        // Log and return the file URL
        String fileUrl = uploadResult.get("secure_url").toString();
        System.out.println("Uploaded file URL: " + fileUrl); // For debugging

        return fileUrl;
    }




}
