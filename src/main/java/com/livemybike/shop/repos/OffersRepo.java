package com.livemybike.shop.repos;

import com.livemybike.shop.entities.Offer;
import org.springframework.data.repository.CrudRepository;

public interface OffersRepo extends CrudRepository<Offer, Long> {
}
