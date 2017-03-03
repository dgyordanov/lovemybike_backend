package com.livemybike.shop.offers;

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

    OfferDto createOffer(Offer offerDto);

    List<OfferDto> listMyOffers();
}
