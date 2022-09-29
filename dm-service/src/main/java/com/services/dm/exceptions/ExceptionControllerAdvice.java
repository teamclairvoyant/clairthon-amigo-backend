package com.services.dm.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    /*
     * catching all unhandled exceptions from Controllers
     */

    /**
     * method to handle Exception.
     *
     * @param ex parameter
     * @return a ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> processException(Exception ex) {
        log.error("Exception caught: ", ex);
        return new ResponseEntity<>(
                new RestErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

}