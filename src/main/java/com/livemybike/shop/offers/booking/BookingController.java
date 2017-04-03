package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.OffersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/offers/{offerId}/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private OffersService offersService;

    @RequestMapping(value = "", method = POST, produces = APPLICATION_JSON_VALUE)
    public BookingDTO requestBooking(@RequestBody BookingDTO bookingDTO) throws InvalidBookingException {
        Booking booking = bookingService.buildBooking(bookingDTO);
        Booking storedBooking = bookingService.requestBooking(booking);
        return new BookingDTO(storedBooking.getId(), storedBooking.getFromDate(), storedBooking.getTo(),
                storedBooking.getRequestedBy().getId(), storedBooking.getOffer().getId());
    }

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<BookingDTO> readOfferBookings(
            @PathVariable(value="offerId") long offerId) {
        return offersService.getOfferBookings(offerId).stream()
                .map(booking -> new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                        booking.getRequestedBy().getId(), booking.getOffer().getId()))
                .collect(Collectors.toList());
    }

}
