package com.brodygaudel.accountservice.command.util.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface CounterRepository extends JpaRepository<Counter, String> {

    @Query("select c from Counter c where c.date =?1")
    Optional<Counter> findByDate(LocalDate localDate);
}
