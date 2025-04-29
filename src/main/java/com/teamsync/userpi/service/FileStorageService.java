package com.teamsync.userpi.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads/";
    private final String uploadDiruser = "C:/Users/USER/IdeaProjects/UserPI/uploads/";


    public String saveFile(MultipartFile file) {
        try {
            // ✅ Créer le dossier s'il n'existe pas
            Files.createDirectories(Paths.get(uploadDir));

            // ✅ Générer un nom unique
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // ✅ Sauvegarder le fichier
            Files.write(filePath, file.getBytes());

            return filePath.toString(); // ✅ Retourner le chemin du fichier
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier", e);
        }
    }


    public String savePhoto(MultipartFile file) {
        try {
            // Create the directory if it doesn't exist (this is optional if it already exists)
            Files.createDirectories(Paths.get(uploadDir));

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("File must have a name");
            }
            // Sanitize the filename: replace spaces with underscores and remove unwanted characters
            String sanitizedFilename = originalFilename.replaceAll(" ", "_")
                    .replaceAll("[^a-zA-Z0-9_\\.\\-]", "");
            String fileName = UUID.randomUUID().toString() + "_" + sanitizedFilename;
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            // Return the URL from which the file can be accessed
            return "http://localhost:8080/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }



}
