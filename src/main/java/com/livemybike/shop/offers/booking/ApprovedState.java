package com.livemybike.shop.offers.booking;

import com.livemybike.shop.security.Account;

import java.util.Date;

class ApprovedState extends AbstractState {

    @Override
    void cancel(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        if (!booking.getOffer().getOwner().equals(currentUser) && !booking.getRequestedBy().equals(currentUser)) {
            throw new InvalidStateTransitionException("User not allowed to cancel the booking");
        }

        if (booking.getFrom().before(new Date())) {
            throw new InvalidStateTransitionException("Booking from date is already in the past");
        }
        booking.setState(State.CANCELED_STATE);
    }

    @Override
    public String getValue() {
        return State.APPROVED_STATE_STRING;
    }
}
