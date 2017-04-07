package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.OffersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingsRepo bookingsRepo;

    @Autowired
    private OffersService offersService;

    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public BookingDTO getBooking(
            @PathVariable(value = "id") long bookingId) {
        Booking booking = bookingsRepo.findOne(bookingId);
        return booking != null ?
                new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                        booking.getRequestedBy().getId(), booking.getOffer().getId())
                : null;
    }

    @RequestMapping(value = "/{id}/@approve", method = GET, produces = APPLICATION_JSON_VALUE)
    public BookingDTO approveBooking(
            @PathVariable(value = "id") long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
        Booking booking = bookingService.approveBooking(bookingId);
        return new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                booking.getRequestedBy().getId(), booking.getOffer().getId());
    }

    @RequestMapping(value = "/{id}/@cancel", method = GET, produces = APPLICATION_JSON_VALUE)
    public BookingDTO cancelBooking(
            @PathVariable(value = "id") long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
        Booking booking = bookingService.cancelBooking(bookingId);
        return new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                booking.getRequestedBy().getId(), booking.getOffer().getId());
    }

    @RequestMapping(value = "/{id}/@reopen", method = GET, produces = APPLICATION_JSON_VALUE)
    public BookingDTO reopenBooking(
            @PathVariable(value = "id") long bookingId) throws InvalidBookingException, InvalidStateTransitionException {
        Booking booking = bookingService.reopenBooking(bookingId);
        return new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                booking.getRequestedBy().getId(), booking.getOffer().getId());
    }

}
