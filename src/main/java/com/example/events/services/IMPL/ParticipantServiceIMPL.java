package com.example.events.services.IMPL;

import com.example.events.entity.Participant;
import com.example.events.repository.ParticipantRepository;
import com.example.events.services.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantServiceIMPL implements IParticipantService {

    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantServiceIMPL(ParticipantRepository participantRepository){
        this.participantRepository = participantRepository;
    }

    @Override
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Participant getParticipantById(String id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));
    }

    @Override
    public void deleteParticipant(String id) {
        participantRepository.deleteById(id);
    }

    @Override
    public Participant addParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public Participant updateParticipant(String id, Participant participantDetails) {
        Participant existingParticipant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participant not found with id: " + id));

        // Update fields
        existingParticipant.setName(participantDetails.getName());
        existingParticipant.setEmail(participantDetails.getEmail());

        return participantRepository.save(existingParticipant);
    }
}
