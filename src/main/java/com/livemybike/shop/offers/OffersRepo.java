package com.livemybike.shop.offers;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.livemybike.shop.security.Account;

public interface OffersRepo extends JpaRepository<Offer, Long> {

    Page<Offer> findByGenderIn(Collection<String> genders, Pageable pageRequest);

    Page<Offer> findByOwner(Account owner, Pageable pageRequest);

    Page<Offer> findByCityIgnoreCaseOrPostcodeIgnoreCase(String location, String postcode, Pageable pageRequest);

    @Query(("SELECT o FROM Offer o WHERE o.gender in ?1 AND (LOWER(o.city) = LOWER(?2) OR LOWER(o.postcode) = LOWER(?2))"))
    Page<Offer> findByGenderAndLocation(Collection<String> genders, String location, Pageable pageRequest);
}
