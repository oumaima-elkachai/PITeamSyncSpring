package com.example.events.services.interfaces;

import com.example.events.entity.Certification;

import java.util.List;

public interface ICertificationService {
    Certification addCertification(Certification certification);
    List<Certification> getAllCertifications();
    Certification getCertificationById(String id);
    void deleteCertification(String id);
    Certification updateCertification(String id, Certification updatedCertification);

    void assignCertification(String certificationId, String participantId, String eventId);

    List<Certification> getCertificationsByParticipant(String participantId);

    List<Certification> getCertificationsByEvent(String eventId);

    void deleteCertificationsByEvent(String eventId);
}
