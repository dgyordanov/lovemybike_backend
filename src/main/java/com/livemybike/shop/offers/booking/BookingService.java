package com.livemybike.shop.offers.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingsRepo bookingsRepo;

    @Transactional
    public Booking requestBooking(Booking newBooking) throws InvalidBookingException {
        int approvedForInterval = bookingsRepo.countApprovedBookingsForInterval(State.APPROVED_STATE,
                newBooking.getFromDate(), newBooking.getTo());
        if (approvedForInterval > 0) {
            throw new InvalidBookingException(
                    String.format("There is already an approved booking for interval %1$tF - %2$tF",
                            newBooking.getFromDate(), newBooking.getTo()));
        }
        return bookingsRepo.save(newBooking);
    }

}
