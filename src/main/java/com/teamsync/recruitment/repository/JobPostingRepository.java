package com.teamsync.recruitment.repository;

import com.teamsync.recruitment.entity.Application;
import com.teamsync.recruitment.entity.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JobPostingRepository extends MongoRepository<JobPosting, String> {
    List<JobPosting> findByCategory(String category);
    List<JobPosting> findByDepartment(String department);
    List<JobPosting> findByJobId(String jobId);

}
