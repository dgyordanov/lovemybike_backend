package com.livemybike.shop.images;

public enum ImageSize {

    SMALL("s", 200), MEDIUM("m", 800), ORIGINAL("o", -1);

    private String value;
    private int height;

    ImageSize(String value, int height) {
        this.value = value;
        this.height = height;
    }

    public String getValue() {
        return value;
    }

    public int getHeight() {
        return height;
    }
}
