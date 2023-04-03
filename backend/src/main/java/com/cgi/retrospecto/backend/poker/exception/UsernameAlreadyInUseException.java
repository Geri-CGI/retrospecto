package com.cgi.retrospecto.backend.poker.exception;

public class UsernameAlreadyInUseException extends Exception {
    public UsernameAlreadyInUseException(String errorMessage) {
        super(errorMessage);
    }
}
