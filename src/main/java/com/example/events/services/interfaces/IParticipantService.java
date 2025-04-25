package com.example.events.services.interfaces;

import com.example.events.entity.Participant;
import java.util.List;

public interface IParticipantService {

    List<Participant> getAllParticipants();

    Participant addParticipant(Participant participant);

    Participant getParticipantById(String id);

    void deleteParticipant(String id);

    Participant updateParticipant(String id, Participant participantDetails);

    String getParticipantName(String participantId);
}
