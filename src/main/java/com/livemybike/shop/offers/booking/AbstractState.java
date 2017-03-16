package com.livemybike.shop.offers.booking;

abstract class AbstractState implements State {

    void request(Booking booking) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be requested");
    }

    void approve(Booking booking) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be approved");
    }

    void cancel(Booking booking) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be canceled");
    }

    void reopen(Booking booking) throws InvalidStateTransitionException {
        throw new InvalidStateTransitionException("Booking can not be reopened");
    }

}
