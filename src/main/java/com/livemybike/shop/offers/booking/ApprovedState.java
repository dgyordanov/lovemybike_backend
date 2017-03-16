package com.livemybike.shop.offers.booking;

class ApprovedState extends AbstractState {

    @Override
    void cancel(Booking booking) {
        booking.setState(State.CANCELED_STATE);
    }

    @Override
    public String getValue() {
        return State.APPROVED_STATE_STRING;
    }
}
