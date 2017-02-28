package com.livemybike.shop.offers;

import com.livemybike.shop.security.Account;
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
    private String street;
    private String number;
    private String postcode;
    private String city;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account owner;

}