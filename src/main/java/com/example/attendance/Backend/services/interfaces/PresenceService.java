package com.example.attendance.Backend.services.interfaces;

import com.example.attendance.Backend.entity.Presence;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface PresenceService {
    List<Presence> getAllAttendances();
    Presence getAttendanceById(String id);
    Presence saveAttendance(Presence presence);
    Presence updateAttendance(String id, Presence presence);
    void deleteAttendance(String id);
}
