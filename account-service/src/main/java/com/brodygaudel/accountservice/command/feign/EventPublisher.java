package com.brodygaudel.accountservice.command.feign;

import com.brodygaudel.accountservice.common.event.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Defines methods for publishing various account-related events using Feign client.
 */
@FeignClient(name = "ACCOUNT-QUERY-SERVICE")
public interface EventPublisher {

    /**
     * Sends an {@link AccountCreatedEvent} to the account query service.
     *
     * @param event The account created event to be sent.
     */
    @PostMapping("/created")
    void send(@RequestBody AccountCreatedEvent event);

    /**
     * Sends an {@link AccountCreditedEvent} to the account query service.
     *
     * @param event The account credited event to be sent.
     */
    @PutMapping("/credited")
    void send(@RequestBody AccountCreditedEvent event);

    /**
     * Sends an {@link AccountDebitedEvent} to the account query service.
     *
     * @param event The account debited event to be sent.
     */
    @PutMapping("/debited")
    void send(@RequestBody AccountDebitedEvent event);

    /**
     * Sends an {@link AccountActivatedEvent} to the account query service.
     *
     * @param event The account activated event to be sent.
     */
    @PutMapping("/activated")
    void send(@RequestBody AccountActivatedEvent event);

    /**
     * Sends an {@link AccountSuspendedEvent} to the account query service.
     *
     * @param event The account suspended event to be sent.
     */
    @PutMapping("/suspended")
    void send(@RequestBody AccountSuspendedEvent event);

    /**
     * Sends an {@link AccountDeletedEvent} to the account query service.
     *
     * @param event The account deleted event to be sent.
     */
    @DeleteMapping("/deleted")
    void send(@RequestBody AccountDeletedEvent event);

}


