package com.example.events.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "event")
public class Event {

    @Id
    private String idEvent;

    private String title;
    private String description;
    private TypeStatus typeS;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private boolean reminderSent = false;
    private String timeZone = "UTC";

    @DBRef(lazy = true)
    @ToString.Exclude
    private List<String> participantId;

    private Integer capacity; // Maximum number of participants allowed

    //  private List<Participant> participants;

    // Date/time convenience methods
    public LocalDateTime getStartDateTime() {
        return startDate.atTime(startTime != null ? startTime : LocalTime.MIN);
    }

    public LocalDateTime getEndDateTime() {
        return endDate.atTime(endTime != null ? endTime : LocalTime.MAX);
    }

    // Constructor with required fields
    public Event(String title, LocalDate startDate) {
        this.title = title;
        this.startDate = startDate;
    }

    // Proper entity equality comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;
        return idEvent != null && idEvent.equals(event.idEvent);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeStatus getTypeS() {
        return typeS;
    }

    public void setTypeS(TypeStatus typeS) {
        this.typeS = typeS;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public List<String> getParticipantId() {
        return participantId;
    }

    public void setParticipantId(List<String> participantId) {
        this.participantId = participantId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}