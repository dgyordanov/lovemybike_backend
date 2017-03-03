package com.livemybike.shop.offers;

import java.util.Collection;
import java.util.List;

import com.livemybike.shop.security.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OffersRepo extends CrudRepository<Offer, Long> {

    List<Offer> findByGenderIn(Collection<String> genders);

    List<Offer> findByOwner(Account owner);

    Iterable<Offer> findByCityIgnoreCaseOrPostcodeIgnoreCase(String location, String location1);

    @Query(("SELECT o FROM Offer o WHERE o.gender in ?1 AND (LOWER(o.city) = LOWER(?2) OR LOWER(o.postcode) = LOWER(?2))"))
    Iterable<Offer> findByGenderAndLocation(Collection<String> genders, String location);
}
