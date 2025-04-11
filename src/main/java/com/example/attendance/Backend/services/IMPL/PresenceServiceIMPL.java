package com.example.attendance.Backend.services.IMPL;

import com.example.attendance.Backend.entity.Employee;
import com.example.attendance.Backend.entity.Presence;
import com.example.attendance.Backend.repository.EmployeeRepository;
import com.example.attendance.Backend.repository.PresenceRepository;
import com.example.attendance.Backend.services.interfaces.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Service
public class PresenceServiceIMPL implements PresenceService {

    @Autowired
    private PresenceRepository presenceRepository;


    private static final Logger logger = LoggerFactory.getLogger(PresenceServiceIMPL.class);

    @Override
    public List<Presence> getAllAttendances() {
        return presenceRepository.findAll();
    }

    @Override
    public Presence getAttendanceById(String id) {
        return presenceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance not found: " + id));
    }

    @Override
    public Presence saveAttendance(Presence presence) {
        logger.info("Saving presence for employee: {}", presence.getEmployeeId());

        calculateWorkedHours(presence);

        return presenceRepository.save(presence);
    }



    public Presence updateAttendance(String id, Presence updatedPresence) {
        Presence presence = presenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presence not found"));

        presence.setDate(updatedPresence.getDate());
        presence.setCheckInTime(updatedPresence.getCheckInTime());
        presence.setCheckOutTime(updatedPresence.getCheckOutTime());
        calculateWorkedHours(presence);

        return presenceRepository.save(presence);
    }

    private void calculateWorkedHours(Presence presence) {
        if (presence.getCheckInTime() != null && presence.getCheckOutTime() != null) {
            Duration duration = Duration.between(presence.getCheckInTime(), presence.getCheckOutTime());
            double hours = duration.toMinutes() / 60.0;
            presence.setHoursWorked(hours);
            presence.setOvertimeHours(Math.max(0, hours - 8));
        }
    }

    @Override
    public void deleteAttendance(String id) {
        presenceRepository.deleteById(id);
    }


}

