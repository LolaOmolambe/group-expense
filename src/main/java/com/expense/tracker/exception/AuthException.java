package com.expense.tracker.exception;

public class AuthException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AuthException(String msg) {
        super(msg);
    }
}
