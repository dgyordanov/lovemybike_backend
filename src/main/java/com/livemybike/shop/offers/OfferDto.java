package com.livemybike.shop.offers;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OfferDto {

    private Long id;
    private BigDecimal price;
    private String title;
    private String image0;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String description;
    private String gender;
    private Long owner_id;
    private String street;
    private String number;
    private String postcode;
    private String city;
}
