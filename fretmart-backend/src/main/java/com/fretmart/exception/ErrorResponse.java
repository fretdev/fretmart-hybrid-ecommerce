package com.fretmart.exception;
import lombok.Value;
import java.time.LocalDateTime;

@Value
public class ErrorResponse {
    int status;
    String message;
    LocalDateTime timestamp;
}
