package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import com.livemybike.shop.security.Account;

import java.util.Calendar;
import java.util.Date;

public abstract class AbstractBookingTest {

    protected static final long OFFER_ID = 234234L;
    protected static final long OFFER_OWNER_ID = 34687L;
    protected static final long REQUESTED_BY_ID = 254574L;
    protected static final long SOMEBODY_ELSE_ID = 897459825L;

    protected Offer getOffer(Account owner) {
        Offer offer = new Offer();
        offer.setId(OFFER_ID);
        offer.setOwner(owner);
        return offer;
    }

    protected Account getOfferOwner() {
        Account account = new Account();
        account.setId(OFFER_OWNER_ID);
        return account;
    }

    protected Account getRequestedBy() {
        Account account = new Account();
        account.setId(REQUESTED_BY_ID);
        return account;
    }

    protected Account getSomebodyElse() {
        Account account = new Account();
        account.setId(SOMEBODY_ELSE_ID);
        return account;
    }

    protected Date getFrom() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        return calendar.getTime();
    }

    protected Date getTo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 10);
        return calendar.getTime();
    }

    protected Date getFromPast() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -10);
        return calendar.getTime();
    }

    protected Booking createValidBooking() {
        Date from = getFrom();
        Date to = getTo();
        Offer offer = getOffer(getOfferOwner());
        Account requestedBy = getRequestedBy();

        return new Booking(from, to, offer, requestedBy);
    }

}
