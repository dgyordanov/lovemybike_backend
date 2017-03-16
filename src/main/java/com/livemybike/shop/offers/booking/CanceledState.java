package com.livemybike.shop.offers.booking;

class CanceledState extends AbstractState {

    @Override
    void request(Booking booking) {
        booking.setState(State.REQUEST_STATE);
    }

    @Override
    public String getValue() {
        return State.CANCELED_STATE_STRING;
    }
}
