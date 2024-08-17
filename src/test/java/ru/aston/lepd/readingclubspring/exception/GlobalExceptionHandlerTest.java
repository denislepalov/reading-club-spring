package ru.aston.lepd.readingclubspring.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();


    @Test
    void handleNotFoundException() {
        final String errorInfo = "Error";
        final NotFoundException exception = new NotFoundException(errorInfo);

        ResponseEntity<ExceptionInfo> actualResult = globalExceptionHandler.handle(exception);

        assertEquals(errorInfo, actualResult.getBody().errorInfo());
        assertEquals(HttpStatus.NOT_FOUND, actualResult.getStatusCode());
    }



    @Test
    void testHandleException() {
        final String errorInfo = "Internal error";
        final Exception exception = new Exception(errorInfo);

        ResponseEntity<ExceptionInfo> actualResult = globalExceptionHandler.handle(exception);

        assertEquals(errorInfo, actualResult.getBody().errorInfo());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResult.getStatusCode());
    }


}