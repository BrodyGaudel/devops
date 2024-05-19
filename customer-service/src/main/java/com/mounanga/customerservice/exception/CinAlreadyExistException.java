package com.mounanga.customerservice.exception;

/**
 * Exception thrown when attempting to create a customer with a Cin that already exists.
 *
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public class CinAlreadyExistException extends RuntimeException{

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CinAlreadyExistException(String message) {
        super(message);
    }
}
