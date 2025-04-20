package com.example.events.services.IMPL;

import com.example.events.entity.Certification;
import com.example.events.repository.ParticipantRepository;
import com.example.events.repository.certificationRepository;
import com.example.events.repository.eventRepository;
import com.example.events.services.interfaces.ICertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationServiceIMPL implements ICertificationService {

    private final certificationRepository certificationRepository;

    @Autowired
    public CertificationServiceIMPL(certificationRepository certificationRepository){
        this.certificationRepository = certificationRepository;
      //  this.participantRepository = participantRepository;
    }

    @Override
    public Certification addCertification(Certification certification) {
        return certificationRepository.save(certification);
    }

    @Override
    public List<Certification> getAllCertifications() {
        return certificationRepository.findAll();
    }

    @Override
    public Certification getCertificationById(String id) {
        return certificationRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCertification(String id) {
        certificationRepository.deleteById(id);
    }

    @Override
    public Certification updateCertification(String id, Certification updatedCertification) {
        Optional<Certification> existing = certificationRepository.findById(id);
        if (existing.isPresent()) {
            Certification cert = existing.get();
            cert.setTitle(updatedCertification.getTitle());
            cert.setDescription(updatedCertification.getDescription());
            cert.setIssueDate(updatedCertification.getIssueDate());
            cert.setEventId(updatedCertification.getEventId());
            cert.setParticipantId(updatedCertification.getParticipantId());
            cert.setCertificateURL(updatedCertification.getCertificateURL());
           // cert.setStatus(updatedCertification.getStatus());
            return certificationRepository.save(cert);
        }
        return null;
    }

    @Override
    public void assignCertification(String certificationId, String participantId, String eventId) {
        Certification certification = certificationRepository.findById(certificationId).orElseThrow();
        certification.setParticipantId(participantId);
        certification.setEventId(eventId);
        certificationRepository.save(certification);
    }

    @Override
    public List<Certification> getCertificationsByParticipant(String participantId) {
        return certificationRepository.findByParticipantId(participantId);
    }

    @Override
    public List<Certification> getCertificationsByEvent(String eventId) {
        return certificationRepository.findByEventId(eventId);
    }

    @Override
    public void deleteCertificationsByEvent(String eventId) {
        certificationRepository.deleteByEventId(eventId);
    }




}