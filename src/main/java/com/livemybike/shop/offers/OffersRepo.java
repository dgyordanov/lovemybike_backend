package com.livemybike.shop.offers;

import com.livemybike.shop.offers.Offer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface OffersRepo extends CrudRepository<Offer, Long> {

    List<Offer> findByGenderIn(Collection<String> genders);

}
