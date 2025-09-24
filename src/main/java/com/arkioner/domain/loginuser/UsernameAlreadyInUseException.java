package com.arkioner.domain.loginuser;

public class UsernameAlreadyInUseException extends RuntimeException {
    private static final String MESSAGE = "Username %s is already in use";
    public UsernameAlreadyInUseException(String username) {
        super(MESSAGE.formatted(username));
    }
}
