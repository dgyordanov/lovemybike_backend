package com.livemybike.shop.offers;

import java.util.List;

public interface OffersService {

    List<OfferDto> listOffers(String genderFilter);

    OfferDto createOffer(Offer offerDto);

}
