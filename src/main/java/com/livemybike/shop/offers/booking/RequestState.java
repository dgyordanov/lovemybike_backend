package com.livemybike.shop.offers.booking;

class RequestState extends AbstractState {

    @Override
    void approve(Booking booking) throws InvalidStateTransitionException {
        booking.setState(State.APPROVED_STATE);
    }

    @Override
    void cancel(Booking booking) throws InvalidStateTransitionException {
        booking.setState(State.CANCELED_STATE);
    }

    @Override
    public String getValue() {
        return State.REQUEST_STATE_STRING;
    }
}
