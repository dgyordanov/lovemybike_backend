package com.livemybike.shop.offers.booking;

import com.livemybike.shop.security.Account;

import java.util.Date;

class CanceledState extends AbstractState {

    @Override
    void reopen(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        if (!booking.getRequestedBy().equals(currentUser)) {
            throw new InvalidStateTransitionException("User not allowed to reopen the booking");
        }

        if (booking.getFrom().before(new Date())) {
            throw new InvalidStateTransitionException("Booking from date is already in the past");
        }
        booking.setState(State.REQUEST_STATE);
    }

    @Override
    public String getValue() {
        return State.CANCELED_STATE_STRING;
    }
}
