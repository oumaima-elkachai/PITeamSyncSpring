package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Presence;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PresenceRepository extends MongoRepository<Presence, String> {
    Optional<Presence> findByEmployeeIdAndDate(String employeeId, LocalDate date);


}
