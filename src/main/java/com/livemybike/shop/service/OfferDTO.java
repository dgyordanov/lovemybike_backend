package com.livemybike.shop.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Offer DTO which represents a transaction in the REST service layer.
 * <p>
 *
 * @author Diyan Yordanov
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OfferDTO {

    private long id;
    // TODO: make a Money value object
    private BigDecimal price;
    private String title;
    private String imageUrl;
    private String description;
    private String gender;
}
