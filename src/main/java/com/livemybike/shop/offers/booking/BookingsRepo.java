package com.livemybike.shop.offers.booking;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingsRepo extends JpaRepository<Booking, Long> {
}
