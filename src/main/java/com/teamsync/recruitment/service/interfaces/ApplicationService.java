package com.teamsync.recruitment.service.interfaces;

import com.teamsync.recruitment.dto.ApplicationDetailsDTO;
import com.teamsync.recruitment.entity.Application;
import java.util.List;
import java.util.Map;

public interface ApplicationService {
    List<Application> getAllApplications();
    Application getApplicationById(String id);
    Application createApplication(Application application, String jobId);
    void updateApplicationStatus(String id, String status);
    void deleteApplication(String id);
    public Map<String, Long> getApplicationStats();
   // public ApplicationDetailsDTO getApplicationWithDetails(String applicationId);
    List<Application> getApplicationsByJobId(String jobId);
    public String extractTextFromCvUrl(String cvUrl);

    List<Application> getRankedApplications(String jobId);
    public Map<String, Long> getApplicationsCountPerJob();
}