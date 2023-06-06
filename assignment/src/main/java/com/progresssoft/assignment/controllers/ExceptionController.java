package com.progresssoft.assignment.controllers;

import java.sql.SQLException;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.progresssoft.assignment.exceptions.DealValidityException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
  
    @ExceptionHandler ({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException e) {
            Set<ConstraintViolation<?>> x=   e.getConstraintViolations();
        return new ResponseEntity<>((e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ({SQLException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> sqlIntegrity(
        SQLException e) {
         
        return new ResponseEntity<>((e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ({DealValidityException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> DtoValidation(
        DealValidityException e) {
         
        return new ResponseEntity<>((e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}