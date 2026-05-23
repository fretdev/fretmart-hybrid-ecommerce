package com.fretmart.exception;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
public class ValidationErrorResponse {
    int status;
    String message;
    Map<String,String> errors;
    LocalDateTime timestamp;
}
