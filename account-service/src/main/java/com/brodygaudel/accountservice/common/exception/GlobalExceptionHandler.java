package com.brodygaudel.accountservice.common.exception;

import org.axonframework.modelling.command.AggregateNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull OperationNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull AccountNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull AggregateNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body(new ExceptionResponse(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(CustomerAlreadyHaveAccountException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull CustomerAlreadyHaveAccountException exception) {
        return ResponseEntity.status(CONFLICT).body(new ExceptionResponse(
                CONFLICT.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }



    @ExceptionHandler(AccountNotActivatedException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull AccountNotActivatedException exception) {
        return ResponseEntity.status(UNAUTHORIZED).body(new ExceptionResponse(
                UNAUTHORIZED.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(AmountNotSufficientException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull AmountNotSufficientException exception) {
        return ResponseEntity.status(FORBIDDEN).body(new ExceptionResponse(
                FORBIDDEN.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(BalanceNotInsufficientException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull BalanceNotInsufficientException exception) {
        return ResponseEntity.status(FORBIDDEN).body(new ExceptionResponse(
                FORBIDDEN.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>(),
                new HashMap<>()
        ));
    }

    @ExceptionHandler(MaxAccountPerDayException.class)
    public ResponseEntity<ExceptionResponse> handleException(@NotNull MaxAccountPerDayException exception) {
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ExceptionResponse(
                TOO_MANY_REQUESTS.value(),
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
