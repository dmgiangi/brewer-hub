package com.github.dmgiangi.brewerhub.models.exceptions;

import lombok.Getter;

import java.sql.SQLException;

@Getter
public class InsertException extends SQLException {
    SQLException throwingException;
    public InsertException(String message, SQLException exception) {
        super(message);
        throwingException = exception;
    }
    public InsertException(String message) {
        super(message);
    }
}
