package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import com.livemybike.shop.offers.OffersRepo;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import com.livemybike.shop.security.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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

    public Booking buildBooking(BookingDTO bookingDTO) throws InvalidBookingException {
        try {
            Offer offer = offersRepo.findOne(bookingDTO.getOfferId());
            Account requestedBy = accountService.getCurrentLoggedIn();
            return new Booking(bookingDTO.getFromDate(), bookingDTO.getToDate(), offer, requestedBy);
        } catch (IllegalArgumentException e) {
            throw new InvalidBookingException(e.getMessage(), e);
        }
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
    public Booking reopenBooking(Long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
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
        booking.reopen(accountService.getCurrentLoggedIn());
        return bookingsRepo.save(booking);
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

    public Booking getBooking(long bookingId) throws InvalidBookingException {
        Booking booking = bookingsRepo.findOne(bookingId);
        if (booking == null) {
            throw new InvalidBookingException(String.format("Booking with ID: %d not found", bookingId));
        }
        Account currentUser = accountService.getCurrentLoggedIn();
        if (!booking.getOffer().getOwner().equals(currentUser) && !booking.getRequestedBy().equals(currentUser)) {
            throw new InvalidBookingException("Booking with ID: %d not found");
        }

        return booking;
    }

    public List<Booking> findApprovedBookingByOfferInInterval(Offer offer, Date startDate, Date endDate) {
        return bookingsRepo.findBookingsByOfferInInterval(State.APPROVED_STATE, offer, startDate, endDate);

    }
}
