package com.example.attendance.Backend.repository;

import com.example.attendance.Backend.entity.Benefit;
import com.example.attendance.Backend.entity.Presence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresenceRepository extends MongoRepository<Presence, String> {
    Optional<Presence> findByEmployeeIdAndDate(String employeeId, LocalDate date);



    List<Presence> findByEmployeeIdAndDateBetween(String employeeId, LocalDate start, LocalDate end);

    List<Presence> findByEmployeeId(String employeeId);
}
