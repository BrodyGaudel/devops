package com.brodygaudel.accountservice.command.model;

public class DeleteAccountCommand extends BaseCommand<String>{
    public DeleteAccountCommand(String id) {
        super(id);
    }
}
