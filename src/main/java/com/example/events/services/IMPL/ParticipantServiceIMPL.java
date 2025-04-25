package com.example.events.services.IMPL;

import com.example.events.entity.Participant;
import com.example.events.exception.ResourceNotFoundException;
import com.example.events.repository.ParticipantRepository;
import com.example.events.services.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantServiceIMPL implements IParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantServiceIMPL(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Participant getParticipantById(String id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));
    }

    @Override
    public void deleteParticipant(String id) {
        // Check if participant exists before deleting
        if (!participantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Participant not found with id: " + id);
        }
        participantRepository.deleteById(id);
    }

    @Override
    public Participant addParticipant(Participant participant) {
        // Add validation for required fields
        if (participant.getEmail() == null || participant.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (participant.getName() == null || participant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        return participantRepository.save(participant);
    }

    @Override
    public Participant updateParticipant(String id, Participant participantDetails) {
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participant not found with id: " + id));

        // Add validation for required fields
        if (participantDetails.getEmail() != null && !participantDetails.getEmail().trim().isEmpty()) {
            existingParticipant.setEmail(participantDetails.getEmail());
        }
        if (participantDetails.getName() != null && !participantDetails.getName().trim().isEmpty()) {
            existingParticipant.setName(participantDetails.getName());
        }

        return participantRepository.save(existingParticipant);
    }

    @Override
    public String getParticipantName(String participantId) {
        return participantRepository.findById(participantId)
                .map(Participant::getName)
                .orElse("Unknown Participant");
    }
}
