package com.livemybike.shop.repos;

import com.livemybike.shop.entities.Offer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface OffersRepo extends CrudRepository<Offer, Long> {

    List<Offer> findByGenderIn(Collection<String> genders);

}
