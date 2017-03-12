package com.livemybike.shop.images;

public class ImageStoringException extends RuntimeException {

    public ImageStoringException(String message) { super(message); }

    public ImageStoringException(String message, Throwable t) { super(message, t); }

}
