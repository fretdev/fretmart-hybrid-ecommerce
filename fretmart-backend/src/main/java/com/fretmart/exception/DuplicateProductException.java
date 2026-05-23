package com.fretmart.exception;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(String productName){
        super("Product already exists with name: "+productName);
    }
}
