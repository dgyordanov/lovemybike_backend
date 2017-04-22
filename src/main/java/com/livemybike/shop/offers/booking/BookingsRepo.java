package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

interface BookingsRepo extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b" +
            " WHERE b.state = ?1" +
            " AND ((b.fromDate between ?2 AND ?3)" +
            " OR (b.to between ?2 AND ?3)" +
            " OR (b.fromDate < ?2 AND b.to > ?3))")
    int countApprovedBookingsForInterval(State state, Date fromDate, Date toDate);

    @Query("SELECT b FROM Booking b" +
            " WHERE b.state = ?1" +
            " AND (b.offer = ?2)" +
            " AND ((b.fromDate between ?3 AND ?4)" +
            " OR (b.to between ?3 AND ?4)" +
            " OR (b.fromDate < ?3 AND b.to > ?4))")
    List<Booking> findBookingsByOfferInInterval(State state, Offer offer, Date startDate, Date endDate);
}
