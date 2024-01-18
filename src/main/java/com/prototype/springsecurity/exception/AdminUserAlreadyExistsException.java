package com.prototype.springsecurity.exception;

public class AdminUserAlreadyExistsException extends RuntimeException{

    public AdminUserAlreadyExistsException(String message) {
        super(message);
    }
}
