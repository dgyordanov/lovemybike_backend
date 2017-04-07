package com.livemybike.shop.offers.booking;

public class InvalidBookingException extends Exception {

    InvalidBookingException(String message) {
        super(message);
    }

    InvalidBookingException(String message, Throwable t) {
        super(message, t);
    }

}
