package com.mounanga.customerservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull CustomerNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(CinAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull CinAlreadyExistException exception) {
        return ResponseEntity.status(CONFLICT).body(new ExceptionResponse(
                CONFLICT.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull EmailAlreadyExistException exception) {
        return ResponseEntity.status(CONFLICT).body(new ExceptionResponse(
                CONFLICT.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(CustomerNotAdultException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull CustomerNotAdultException exception) {
        return ResponseEntity.status(FORBIDDEN).body(new ExceptionResponse(
                FORBIDDEN.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull MethodArgumentNotValidException exception) {
        Set<String> validationErrors = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponse(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                validationErrors,
                new HashMap<>()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }
}
