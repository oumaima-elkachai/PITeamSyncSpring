package com.example.payroll.repository;

import com.example.payroll.model.Payroll;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PayrollRepository extends MongoRepository<Payroll, String> {
    List<Payroll> findByEmployeeId(String employeeId);
    Optional<Payroll> findTopByEmployeeIdOrderByPayDateDesc(String employeeId);

}
