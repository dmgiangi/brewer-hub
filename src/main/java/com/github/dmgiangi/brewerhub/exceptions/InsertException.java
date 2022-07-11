package com.github.dmgiangi.brewerhub.exceptions;

import java.sql.SQLException;

public class InsertException extends SQLException {
    Exception throwingException;
    public InsertException(String message, Exception exception) {
        super(message);
        throwingException = exception;
    }
    public InsertException(String message) {
        super(message);
    }
}
