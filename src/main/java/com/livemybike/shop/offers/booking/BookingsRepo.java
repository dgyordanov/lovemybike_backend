package com.livemybike.shop.offers.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

interface BookingsRepo extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b" +
            " WHERE b.state = ?1" +
            " AND ((b.fromDate between ?2 AND ?3)" +
            " OR (b.to between ?2 AND ?3)" +
            " OR (b.fromDate < ?2 AND b.to > ?3))")
    int countApprovedBookingsForInterval(State state, Date fromDate, Date toDate);

}