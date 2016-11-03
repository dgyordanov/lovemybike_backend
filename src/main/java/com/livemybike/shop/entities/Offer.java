package com.livemybike.shop.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "offers")
@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal price;
    private String title;
    @Column(name = "IMAGEURL")
    private String imageUrl;
    private String description;
    private String gender;

}