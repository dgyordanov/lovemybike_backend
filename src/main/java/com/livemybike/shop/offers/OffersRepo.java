package com.livemybike.shop.offers;

import java.util.Collection;
import java.util.List;

import com.livemybike.shop.security.Account;
import org.springframework.data.repository.CrudRepository;

public interface OffersRepo extends CrudRepository<Offer, Long> {

    List<Offer> findByGenderIn(Collection<String> genders);

    List<Offer> findByOwner(Account owner);
}
