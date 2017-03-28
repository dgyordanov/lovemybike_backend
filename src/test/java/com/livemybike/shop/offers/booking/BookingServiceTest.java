package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import com.livemybike.shop.offers.OffersRepo;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingServiceTest extends AbstractBookingTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private BookingsRepo bookingsRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Test
    public void requestBookingTest() throws InvalidBookingException {
        Offer offer = offersRepo.findOne(1L);
        Account requestedBy = accountRepo.findOne(2L);
        Date from = getFrom();
        Date to = getTo();
        Booking booking = new Booking(from, to, offer, requestedBy);

        Booking savedBooking = bookingService.requestBooking(booking);

        assertThat(savedBooking.getId(), is(greaterThan(0L)));
        assertThat(savedBooking.getState(), equalTo(State.REQUEST_STATE));
    }

    @Test
    public void request2BookingSameIntervalTest() throws InvalidBookingException {
        Offer offer = offersRepo.findOne(1L);
        Account requestedBy2 = accountRepo.findOne(2L);
        Date from = getFrom();
        Date to = getTo();
        Booking booking1 = new Booking(from, to, offer, requestedBy2);

        Account requestedBy3 = accountRepo.findOne(3L);
        Booking booking2 = new Booking(from, to, offer, requestedBy3);

        Booking savedBooking1 = bookingService.requestBooking(booking1);
        Booking savedBooking2 = bookingService.requestBooking(booking2);

        assertThat(savedBooking1.getId(), is(greaterThan(0L)));
        assertThat(savedBooking2.getId(), is(greaterThan(0L)));
    }

}
