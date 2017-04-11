package com.livemybike.shop.images;

public class ImageUtil {

    public static String buildImageName(Long offerId, String imageSize, String imageName) {
        StringBuilder result  = new StringBuilder();
        result.append(offerId).append(imageSize).append("_").append(imageName);
        return result.toString();
    }

}
