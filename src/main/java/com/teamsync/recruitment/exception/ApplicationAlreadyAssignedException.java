package com.teamsync.recruitment.exception;

public class ApplicationAlreadyAssignedException extends RuntimeException {
    public ApplicationAlreadyAssignedException(String message) {
        super(message);
    }
}