package ru.aston.lepd.readingclubspring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handle(NotFoundException e) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(e.getMessage());
        return new ResponseEntity<>(exceptionInfo, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionInfo> handle(Exception e) {
        ExceptionInfo exceptionInfo = new ExceptionInfo(e.getMessage());
        return new ResponseEntity<>(exceptionInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
