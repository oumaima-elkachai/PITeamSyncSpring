package com.example.events.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "certifications")
public class Certification {

    @Id
    String idCertification;

    String title;
    String description;

    LocalDate issueDate;

    String eventId;
    String participantId;

    String certificateURL;  // URL to download/view the certificate

    public void setIdCertification(String idCertification) {
        this.idCertification = idCertification;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public void setCertificateURL(String certificateURL) {
        this.certificateURL = certificateURL;
    }

    public String getIdCertification() {
        return idCertification;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public String getCertificateURL() {
        return certificateURL;
    }

    @Override
    public String toString() {
        return "Certification{" +
                "idCertification='" + idCertification + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", issueDate=" + issueDate +
                ", eventId='" + eventId + '\'' +
                ", participantId='" + participantId + '\'' +
                ", certificateURL='" + certificateURL + '\'' +
                '}';
    }
}
