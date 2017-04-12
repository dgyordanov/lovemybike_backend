package com.livemybike.shop.offers;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OfferDto {

    private Long id;
    private BigDecimal price;
    private String title;
    private String image0_s;
    private String image0_m;
    private String image1_s;
    private String image1_m;
    private String image2_s;
    private String image2_m;
    private String image3_s;
    private String image3_m;
    private String image4_s;
    private String image4_m;
    private String image5_s;
    private String image5_m;
    private String description;
    private String gender;
    private Long owner_id;
    private String street;
    private String number;
    private String postcode;
    private String city;
    private String ownerName;
}
