package com.mounanga.customerservice.exception;

/**
 * This exception is thrown to indicate that a customer is not considered an adult.
 * It is a runtime exception, which means it does not need to be explicitly caught
 * or declared by the methods that might throw it.
 * @author Brody Gaudel MOUNANGA BOUKA
 * @since 2024
 * @version 3.0
 */
public class CustomerNotAdultException extends RuntimeException {

    /**
     * Constructs a new CustomerNotAdultException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CustomerNotAdultException(String message) {
        super(message);
    }
}

