package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
