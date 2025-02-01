package com.storesmanagementsystem.gateway.exceptions;

public class AuthenticationException extends RuntimeException{

    String message;
    public AuthenticationException(){

    }

    public AuthenticationException(String msg){
        this.message = msg;
    }
}
