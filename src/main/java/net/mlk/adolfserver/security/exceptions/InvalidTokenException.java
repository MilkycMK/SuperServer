package net.mlk.adolfserver.security.exceptions;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() {
        super("User token is invalid");
    }

}
