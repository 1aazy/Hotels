package org.max.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.max.exceptions.response.CommonExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonExceptionResponse> handleValidationExceptions(ValidationException e) {
        CommonExceptionResponse response = new CommonExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonExceptionResponse> handleEntityNotFoundExceptions(EntityNotFoundException e) {
        CommonExceptionResponse response = new CommonExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        CommonExceptionResponse response;
        if (e.getCause() != null && e.getCause().getCause() instanceof DateTimeParseException) {
            response = new CommonExceptionResponse("Invalid time format. Please provide the time in the format HH:mm.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response = new CommonExceptionResponse("Failed to process the request.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        CommonExceptionResponse response = new CommonExceptionResponse(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
