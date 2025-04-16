package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Benefit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BenefitRepository extends MongoRepository<Benefit, String> {
    List<Benefit> findByEmployeeId(String employeeId);

    boolean existsByEmployeeIdAndType(String employeeId, String type);


}
