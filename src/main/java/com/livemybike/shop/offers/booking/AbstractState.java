package com.livemybike.shop.offers.booking;

import com.livemybike.shop.security.Account;

abstract class AbstractState implements State {

    void approve(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be approved");
    }

    void cancel(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be canceled");
    }

    void reopen(Booking booking, Account currentUser) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be reopened");
    }

}
