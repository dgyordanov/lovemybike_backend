package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import com.livemybike.shop.offers.OffersRepo;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import com.livemybike.shop.security.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingsRepo bookingsRepo;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private AccountRepo accountRepo;

    public Booking buildBooking(BookingDTO bookingDTO) {
        Offer offer = offersRepo.findOne(bookingDTO.getOfferId());
        Account requestedBy = accountRepo.findOne(bookingDTO.getRequestedById());
        return new Booking(bookingDTO.getFromDate(), bookingDTO.getToDate(), offer, requestedBy);
    }

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

    @Transactional
    public Booking approveBooking(Long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
        Booking booking = bookingsRepo.findOne(bookingId);
        if (booking == null) {
            throw new InvalidBookingException(String.format("Booking with ID: %d not found", bookingId));
        }

        int approvedForInterval = bookingsRepo.countApprovedBookingsForInterval(State.APPROVED_STATE,
                booking.getFromDate(), booking.getTo());

        if (approvedForInterval > 0) {
            throw new InvalidBookingException(
                    String.format("There is already an approved booking for interval %1$tF - %2$tF",
                            booking.getFromDate(), booking.getTo()));
        }

        booking.approve(accountService.getCurrentLoggedIn());

        return bookingsRepo.save(booking);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
        Booking booking = bookingsRepo.findOne(bookingId);
        if (booking == null) {
            throw new InvalidBookingException(String.format("Booking with ID: %d not found", bookingId));
        }

        booking.cancel(accountService.getCurrentLoggedIn());

        return bookingsRepo.save(booking);
    }

}
