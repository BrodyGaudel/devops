package com.brodygaudel.accountservice.command.util.implementation;

import com.brodygaudel.accountservice.command.util.IdGenerator;
import com.brodygaudel.accountservice.common.exception.MaxAccountPerDayException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service class responsible for generating unique IDs.
 * Implements the IdGenerator interface.
 * @since 2024
 * @version 3.0
 * @author Brody Gaudel MOUNANGA BOUKA
 */
@Service
@Transactional
public class Generator implements IdGenerator {

    /** Date formatter for generating date-based IDs */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /** Maximum number of accounts that can be generated per day */
    private static final int MAX_ACCOUNTS_PER_DAY = 99999999;

    /** Repository for accessing and storing counter values */
    private final CounterRepository counterRepository;

    /**
     * Constructor for Generator class.
     * @param counterRepository The repository for counter values.
     */
    public Generator(CounterRepository counterRepository) {
        this.counterRepository = counterRepository;
    }


    /**
     * Generates a unique ID based on the current date and a counter value.
     * @return The generated unique ID.
     * @throws MaxAccountPerDayException if the maximum account limit for the day is reached.
     */
    @Override
    public String autoGenerate() {
        LocalDate today = LocalDate.now();
        Optional<Counter> optionalCounter = counterRepository.findByDate(today);

        Counter counter;
        counter = optionalCounter.orElseGet(() -> Counter.builder().count(0).date(today).build());

        if (counter.getCount() >= MAX_ACCOUNTS_PER_DAY) {
            throw new MaxAccountPerDayException("Bank account limit reached for today.");
        }

        int ctr = counter.incrementCount();
        counterRepository.save(counter);

        String formattedDate = today.format(DATE_FORMATTER);
        String formattedCounter = String.format("%08d", ctr);

        return formattedDate + formattedCounter;
    }
}
