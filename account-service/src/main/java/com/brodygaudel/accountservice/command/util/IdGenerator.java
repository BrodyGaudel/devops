package com.brodygaudel.accountservice.command.util;

import com.brodygaudel.accountservice.common.exception.MaxAccountPerDayException;

/**
 * Interface for generating unique IDs.
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
public interface IdGenerator {

    /**
     * Generates a unique ID based on the current date and a counter value.
     * @return The generated unique ID.
     * @throws MaxAccountPerDayException if the maximum account limit for the day is reached.
     */
    String autoGenerate() throws MaxAccountPerDayException;
}
