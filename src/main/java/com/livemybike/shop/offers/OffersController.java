package com.livemybike.shop.offers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.livemybike.shop.offers.booking.Booking;
import com.livemybike.shop.offers.booking.BookingDTO;
import com.livemybike.shop.offers.booking.BookingService;
import com.livemybike.shop.offers.booking.InvalidBookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest controller which exposes offers endpoints.
 *
 * @author Diyan Yordanov
 */
@RestController
@RequestMapping("/offers")
public class OffersController {

    @Autowired
    private OffersService offerService;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "", method = GET, produces = APPLICATION_JSON_VALUE)
    public Page<OfferDto> read(
            @RequestParam(value = "gender", required = false) String genderFilter,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "pageNumber") int pageNumber) {
        return offerService.listOffers(genderFilter, location, pageNumber);
    }

    @RequestMapping(value = "/@my", method = GET, produces = APPLICATION_JSON_VALUE)
    public Page<OfferDto> readMyOffers(@RequestParam(value = "pageNumber") int pageNumber) {
        return offerService.listMyOffers(pageNumber);
    }

    @RequestMapping(value="", method=POST, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody OfferDto postOffer(
            @RequestParam("title") String title,
            @RequestParam("price") String price,
            @RequestParam(value = "gender") String gender,
            @RequestParam("description") String description,
            @RequestParam("street") String street,
            @RequestParam("number") String number,
            @RequestParam("postcode") String postcode,
            @RequestParam("city") String city,
            @RequestParam("image0") MultipartFile image0,
            @RequestParam(value = "image1", required = false) MultipartFile image1,
            @RequestParam(value = "image2", required = false) MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4,
            @RequestParam(value = "image5", required = false) MultipartFile image5) {

            return offerService.createOffer(title, price, gender, description, street,
                    number, postcode, city, image0, image1, image2, image3, image4, image5);
    }

    @RequestMapping(value = "/{offerId}/bookings", method = POST, produces = APPLICATION_JSON_VALUE)
    public BookingDTO requestBooking(
            @RequestBody BookingDTO bookingDTO,
            @PathVariable Long offerId) throws InvalidBookingException {
        Booking booking = bookingService.buildBooking(new BookingDTO(null, bookingDTO.getFromDate()
                , bookingDTO.getToDate(), null, offerId));
        Booking storedBooking = bookingService.requestBooking(booking);
        return new BookingDTO(storedBooking.getId(), storedBooking.getFromDate(), storedBooking.getTo(),
                storedBooking.getRequestedBy().getId(), storedBooking.getOffer().getId());
    }

    @RequestMapping(value = "/{offerId}/bookings", method = GET, produces = APPLICATION_JSON_VALUE)
    public List<BookingDTO> readOfferBookings(
            @PathVariable(value="offerId") long offerId) {
        return offerService.getOfferBookings(offerId).stream()
                .map(booking -> new BookingDTO(booking.getId(), booking.getFromDate(), booking.getTo(),
                        booking.getRequestedBy().getId(), booking.getOffer().getId()))
                .collect(Collectors.toList());
    }

}
