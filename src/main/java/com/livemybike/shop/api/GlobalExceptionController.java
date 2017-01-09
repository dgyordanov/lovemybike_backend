package com.livemybike.shop.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.livemybike.shop.security.AnonymousAuthNotAllowedException;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Handle exception and match them to a proper HTTP response
 *
 * @author Diyan Yordanov
 */
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(AnonymousAuthNotAllowedException.class)
    public ResponseEntity<Error> handleAuthException(AnonymousAuthNotAllowedException e) {
        return new ResponseEntity(new Error(e.getMessage()), HttpStatus.UNAUTHORIZED);

    }

}

@Data
@AllArgsConstructor
class Error {
    private String message;
}