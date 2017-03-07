package com.livemybike.shop.offers;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OffersService {

    /**
     * Search offers by particular criteria
     * 
     * @param genderFilter
     *            Concatenated desired gender chars
     * @param location
     *            Postcode or city
     * @return offers that fits to the desired criteria
     */
    List<OfferDto> listOffers(String genderFilter, String location);

    OfferDto createOffer(String title, String price, String gender, String description, String street,
                         String number, String postcode, String city, MultipartFile image0, MultipartFile image1,
                         MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5);

    List<OfferDto> listMyOffers();
}
