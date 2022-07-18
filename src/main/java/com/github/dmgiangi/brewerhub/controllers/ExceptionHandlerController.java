package com.github.dmgiangi.brewerhub.controllers;

import com.github.dmgiangi.brewerhub.models.entity.ErrorResponse;
import com.github.dmgiangi.brewerhub.models.exceptions.ConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleNoSuchElementFoundException(ConnectionException e, HttpServletResponse response) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value() + ""
                        )
                );
    }
}
