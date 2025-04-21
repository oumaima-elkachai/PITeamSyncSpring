package tn.esprit.spring.teamsync.Services.MPL;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}