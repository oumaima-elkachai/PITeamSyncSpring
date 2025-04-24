package com.teamsync.recruitment.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.teamsync.recruitment.entity.JobPosting;
import com.teamsync.recruitment.exception.JobNotFoundException;
import com.teamsync.recruitment.repository.ApplicationRepository;
import com.teamsync.recruitment.repository.JobPostingRepository;
import com.teamsync.recruitment.service.CloudinaryService;
import com.teamsync.recruitment.service.interfaces.JobPostingService;
import lombok.RequiredArgsConstructor;  // Utilisation de RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor  // Utilisation de Lombok pour générer le constructeur avec les paramètres nécessaires
@Service
public class JobPostingServiceImpl implements JobPostingService {

    private final Cloudinary cloudinary;
    private final JobPostingRepository jobPostingRepository;
    private final ApplicationRepository applicationRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    @Override
    public JobPosting getJobPostingById(String id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException("Job posting not found with id: " + id));
    }

    @Override
    public JobPosting createJobPosting(JobPosting jobPosting) {
        jobPosting.setDatePosted(new Date());
        return jobPostingRepository.save(jobPosting);
    }

    @Override
    public JobPosting updateJobPosting(String id, JobPosting jobPosting) {
        JobPosting existingJob = getJobPostingById(id);
        existingJob.setTitle(jobPosting.getTitle());
        existingJob.setDescription(jobPosting.getDescription());
        existingJob.setDepartment(jobPosting.getDepartment());
        existingJob.setCategory(jobPosting.getCategory());
        existingJob.setSalary(jobPosting.getSalary());
        existingJob.setExpirationDate(jobPosting.getExpirationDate());
        existingJob.setStatus(jobPosting.getStatus());

        // Met à jour l'URL de l'image si elle a été changée

        // Si l'image URL a été modifiée, on met à jour
        existingJob.setImageUrl(jobPosting.getImageUrl());
        return jobPostingRepository.save(existingJob);
    }

    @Override
    public void deleteJobPosting(String id) {
        jobPostingRepository.deleteById(id);
    }

    // Méthode pour uploader l'image sur Cloudinary
    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url");  // Retourner l'URL de l'image
    }

    public JobPosting findJobWithMostApplications() {
        List<JobPosting> allJobs = jobPostingRepository.findAll();
        return allJobs.stream()
                .max(Comparator.comparingInt(job -> applicationRepository.countByJobId(job.getId())))
                .orElse(null);
    }




}
