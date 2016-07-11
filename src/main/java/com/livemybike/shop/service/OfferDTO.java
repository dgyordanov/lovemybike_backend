package com.livemybike.shop.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Offer DTO which represents a transaction in the REST service layer.
 * <p>
 *
 * @author Diyan Yordanov
 */
public class OfferDTO {

    // TODO: make a Money value object
    private BigDecimal price;

    private String model;

    private LocalDate constructionDate;

    // TODO: address value object
    private String address;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getConstructionDate() {
        return constructionDate;
    }

    public void setConstructionDate(LocalDate constructionDate) {
        this.constructionDate = constructionDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
