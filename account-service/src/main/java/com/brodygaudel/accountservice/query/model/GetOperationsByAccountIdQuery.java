package com.brodygaudel.accountservice.query.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetOperationsByAccountIdQuery {
    private String accountId;
    private int page;
    private int size;
}
