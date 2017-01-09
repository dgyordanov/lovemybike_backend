package com.livemybike.shop.security;

/**
 * This exception should be thrown if somebody is trying to execute a not authorized operation
 *
 * @author Diyan Yordanov
 */
public class AnonymousAuthNotAllowedException extends RuntimeException {

    public AnonymousAuthNotAllowedException(String message) {
        super(message);
    }

}
