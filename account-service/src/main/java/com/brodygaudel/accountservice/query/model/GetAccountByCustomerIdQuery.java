package com.brodygaudel.accountservice.query.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetAccountByCustomerIdQuery {
    private String customerId;
}