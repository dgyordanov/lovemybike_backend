package com.livemybike.shop.offers.booking;

import com.livemybike.shop.security.Account;

import java.util.Date;

class RequestState extends AbstractState {

    @Override
    void approve(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        // TODO: when booking is approved, we should make sure that there is no other booking for this period
        if (!booking.getOffer().getOwner().equals(currentUser)) {
            throw new InvalidStateTransitionException("User not allowed to approve the booking");
        }

        if (booking.getFromDate().before(new Date())) {
            throw new InvalidStateTransitionException("Booking from date is already in the past");
        }
        booking.setState(State.APPROVED_STATE);
    }

    @Override
    void cancel(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        if (!booking.getOffer().getOwner().equals(currentUser) && !booking.getRequestedBy().equals(currentUser)) {
            throw new InvalidStateTransitionException("User not allowed to cancel the booking");
        }

        if (booking.getFromDate().before(new Date())) {
            throw new InvalidStateTransitionException("Booking from date is already in the past");
        }
        booking.setState(State.CANCELED_STATE);
    }

    @Override
    public String getValue() {
        return State.REQUEST_STATE_STRING;
    }
}
