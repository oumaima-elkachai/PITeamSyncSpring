package com.example.payroll.repository;

import com.example.payroll.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
