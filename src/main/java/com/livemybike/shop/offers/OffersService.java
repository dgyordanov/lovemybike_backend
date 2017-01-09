package com.livemybike.shop.offers;

public interface OffersService {

    Iterable<Offer> listOffers(String genderFilter);

    Offer createOffer(Offer offerDto);

}
