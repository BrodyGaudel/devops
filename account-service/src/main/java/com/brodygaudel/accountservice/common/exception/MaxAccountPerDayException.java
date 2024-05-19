package com.brodygaudel.accountservice.common.exception;

/**
 * Exception thrown when the maximum account limit for the day is reached.
 * @version 3.0
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 */
public class MaxAccountPerDayException extends RuntimeException{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public MaxAccountPerDayException(String message) {
        super(message);
    }
}
