package com.livemybike.shop.offers;

import com.livemybike.shop.offers.booking.*;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import com.livemybike.shop.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class OffersServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OffersService offersService;

    public void authenticate(String user, String pass) {
        AuthenticationManager authenticationManager = this.context
                .getBean(AuthenticationManager.class);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user, pass));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void bookedDaysNoBookings() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        Date startInterval = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endInterval = calendar.getTime();

        List<Date> result = offersService.getBookedDaysForInterval(1L, startInterval, endInterval);

        assertThat(result, is(empty()));
    }

    @Test
    public void bookedDaysOneBooking() throws InvalidBookingException, InvalidStateTransitionException {
        Offer offer = offersRepo.findOne(1L);

        authenticate(offer.getOwner().getEmail(), offer.getOwner().getPassword());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        Date from = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date to = calendar.getTime();
        Account requestedBy = accountRepo.findOne(2L);
        Booking booking = new Booking(from, to, offer, requestedBy);

        Booking savedBooking = bookingService.requestBooking(booking);
        bookingService.approveBooking(savedBooking.getId());

        calendar.add(Calendar.DATE, -10);
        Date startInterval = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endInterval = calendar.getTime();
        List<Date> result = offersService.getBookedDaysForInterval(1L, startInterval, endInterval);

        assertThat(result, hasItem(DateUtil.getBeginningOfDay(from)));
        assertThat(result, hasItem(DateUtil.getBeginningOfDay(to)));
        assertThat(result, hasSize(3));

    }

    @Test
    public void bookedDaysTwoBooking() throws InvalidBookingException, InvalidStateTransitionException {
        Offer offer = offersRepo.findOne(1L);

        authenticate(offer.getOwner().getEmail(), offer.getOwner().getPassword());

        Calendar calendar = Calendar.getInstance();

        Account requestedBy = accountRepo.findOne(2L);

        calendar.add(Calendar.DATE, 1);
        Date from1 = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date to1 = calendar.getTime();
        Booking booking1 = new Booking(from1, to1, offer, requestedBy);
        Booking savedBooking1 = bookingService.requestBooking(booking1);
        bookingService.approveBooking(savedBooking1.getId());

        calendar.add(Calendar.DATE, 1);
        Date from2 = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date to2 = calendar.getTime();
        Booking booking = new Booking(from2, to2, offer, requestedBy);
        Booking savedBooking = bookingService.requestBooking(booking);
        bookingService.approveBooking(savedBooking.getId());

        calendar.add(Calendar.DATE, -10);
        Date startInterval = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endInterval = calendar.getTime();
        List<Date> result = offersService.getBookedDaysForInterval(1L, startInterval, endInterval);

        assertThat(result, hasItem(DateUtil.getBeginningOfDay(from1)));
        assertThat(result, hasItem(DateUtil.getBeginningOfDay(to1)));
        assertThat(result, hasItem(DateUtil.getBeginningOfDay(from2)));
        assertThat(result, hasItem(DateUtil.getBeginningOfDay(to2)));
        assertThat(result, hasSize(6));

    }

    @Test
    public void bookedDaysRequestedBookingDoesntCount() throws InvalidBookingException, InvalidStateTransitionException {
        Offer offer = offersRepo.findOne(1L);

        authenticate(offer.getOwner().getEmail(), offer.getOwner().getPassword());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        Date from = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date to = calendar.getTime();
        Account requestedBy = accountRepo.findOne(2L);
        Booking booking = new Booking(from, to, offer, requestedBy);

        bookingService.requestBooking(booking);

        calendar.add(Calendar.DATE, -10);
        Date startInterval = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endInterval = calendar.getTime();
        List<Date> result = offersService.getBookedDaysForInterval(1L, startInterval, endInterval);

        assertThat(result, is(empty()));

    }

    @Test
    public void bookedDaysStartIntervalAfterStartDate() throws InvalidBookingException, InvalidStateTransitionException {
        Offer offer = offersRepo.findOne(1L);

        authenticate(offer.getOwner().getEmail(), offer.getOwner().getPassword());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7);
        Date from = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date to = calendar.getTime();
        Account requestedBy = accountRepo.findOne(2L);
        Booking booking = new Booking(from, to, offer, requestedBy);

        Booking savedBooking = bookingService.requestBooking(booking);
        bookingService.approveBooking(savedBooking.getId());

        calendar.add(Calendar.DATE, -5);
        Date startInterval = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date endInterval = calendar.getTime();
        List<Date> result = offersService.getBookedDaysForInterval(1L, startInterval, endInterval);

        assertThat(result, hasItem(startInterval));
        assertThat(result, not(hasItem(from)));

    }

}