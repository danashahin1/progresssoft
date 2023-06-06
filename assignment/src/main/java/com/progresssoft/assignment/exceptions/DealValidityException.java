package com.progresssoft.assignment.exceptions;


public class DealValidityException extends Exception {

    public DealValidityException() {
        super();
    }

    public DealValidityException(String message) {
        super(message);
    }

    public DealValidityException(String message, Throwable cause) {
        super(message, cause);
    }

    public DealValidityException(Throwable cause) {
        super(cause);
    }
}