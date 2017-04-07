package com.livemybike.shop.api;

import com.livemybike.shop.images.ImageStoringException;
import com.livemybike.shop.offers.booking.InvalidBookingException;
import com.livemybike.shop.offers.booking.InvalidStateTransitionException;
import com.livemybike.shop.security.AnonymousAuthNotAllowedException;
import com.livemybike.shop.security.AuthorizationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handle exception and match them to a proper HTTP response
 *
 * @author Diyan Yordanov
 */
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(AnonymousAuthNotAllowedException.class)
    public ResponseEntity<Error> handleAuthException(AnonymousAuthNotAllowedException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Error> handleAuthException(AuthorizationException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ImageStoringException.class)
    public ResponseEntity<Error> handleImageStoringException(ImageStoringException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<Error> handleInvalidBookingException(InvalidBookingException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Error> handleInvalidStateTransitionException(InvalidStateTransitionException e) {
        return new ResponseEntity<>(new Error(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}

@Data
@AllArgsConstructor
class Error {
    private String message;
}