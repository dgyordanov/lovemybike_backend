package com.livemybike.shop.images;

public interface ImageRepo {

    void save(Image image, Long offerId);

    String BASE_IMAGE_URL = "https://s3.eu-central-1.amazonaws.com/bike-bucket/";

}
