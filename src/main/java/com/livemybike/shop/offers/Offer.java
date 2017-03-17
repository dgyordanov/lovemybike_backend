package com.livemybike.shop.offers;

import com.livemybike.shop.offers.booking.Booking;
import com.livemybike.shop.security.Account;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "offers")
@Data
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private String street;
    private String number;
    private String postcode;
    private String city;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account owner;

    @OneToMany(mappedBy="offer")
    private List<Booking> bookings;

}