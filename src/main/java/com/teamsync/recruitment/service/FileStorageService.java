package com.teamsync.recruitment.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "D:\\Users\\NIDHAL\\Desktop\\recruitment\\uploads"; // Remplace avec un chemin valide
    //private static final String uploadDir = "D:\\Users\\NIDHAL\\Desktop\\recruitment\\uploads\\job_img";
    private static final String uploadDir = "D:/Users/NIDHAL/Desktop/recruitment/uploads/job_img/";

    public String saveFile(MultipartFile file) {
        try {
            // ✅ Organiser par année et mois
            LocalDate today = LocalDate.now();
            String yearMonth = today.getYear() + "/" + today.getMonthValue();
            Path uploadPath = Paths.get(UPLOAD_DIR, yearMonth);
            Files.createDirectories(uploadPath);

            // ✅ Générer un nom unique
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(uniqueFileName);

            file.transferTo(filePath.toFile());

            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du stockage du fichier : " + e.getMessage());
        }
    }

    public String savePhoto(MultipartFile file) {
        try {
            // Crée le répertoire si nécessaire
            Files.createDirectories(Paths.get(uploadDir));

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("Le fichier doit avoir un nom");
            }

            // Nettoyage du nom de fichier
            String sanitizedFilename = originalFilename.replaceAll(" ", "_")
                    .replaceAll("[^a-zA-Z0-9_\\.\\-]", "");

            // Génère un nom unique pour le fichier
            String fileName = UUID.randomUUID().toString() + "_" + sanitizedFilename;

            // Chemin complet pour le fichier
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            // Retourne l'URL relative pour accéder à l'image
            return "/uploads/job_img/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Échec de l'enregistrement du fichier", e);
        }
    }


}




