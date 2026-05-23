package com.fretmart.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(long id){
        super("Product not found with id: "+id);
    }
    public ProductNotFoundException(String message){
        super(message);
    }
}
