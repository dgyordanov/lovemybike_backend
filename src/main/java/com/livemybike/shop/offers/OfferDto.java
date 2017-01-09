package com.livemybike.shop.offers;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OfferDto {

    private Long id;
    private BigDecimal price;
    private String title;
    private String imageUrl;
    private String description;
    private String gender;
    private Long owner_id;
}
