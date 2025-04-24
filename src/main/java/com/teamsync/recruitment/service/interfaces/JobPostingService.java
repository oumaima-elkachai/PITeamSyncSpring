package com.teamsync.recruitment.service.interfaces;

import com.teamsync.recruitment.entity.JobPosting;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface JobPostingService {
    List<JobPosting> getAllJobPostings();
    JobPosting getJobPostingById(String id);
    JobPosting createJobPosting(JobPosting jobPosting);
    JobPosting updateJobPosting(String id, JobPosting jobPosting);
    void deleteJobPosting(String id);
    public String uploadImage(MultipartFile file) throws Exception;
    public JobPosting findJobWithMostApplications();
}
