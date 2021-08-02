package it.unipd.dei.webapp.auth;

/**
 * Auth exception
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 */

public class AuthException extends Exception{
    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }
}
