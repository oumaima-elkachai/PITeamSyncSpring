package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByEmployeeId(String employeeId);
}