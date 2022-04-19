package com.kalaerik.productmng.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(DataNotFound.class)
    public ResponseEntity<Object> dataNotFoundExceptionHandling(Exception exception, WebRequest request) {
        return new ResponseEntity<>(new ExceptionDetail(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        //Map<String,String> errors= new HashMap<>();
        List<ExceptionDetail> errors = new ArrayList();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.add(new ExceptionDetail(new Date(), error.getDefaultMessage(), ((FieldError) error).getField()));
            //String name = ((FieldError) error).getField();
            //String message = error.getDefaultMessage();
            //errors.put(name, message);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }
}
