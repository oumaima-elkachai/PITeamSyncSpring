package com.teamsync.recruitment.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StatusChange {
    private String status;
    private LocalDateTime changedAt;

    public StatusChange(String status, LocalDateTime changedAt) {
        this.status = status;
        this.changedAt = changedAt;
    }

}
