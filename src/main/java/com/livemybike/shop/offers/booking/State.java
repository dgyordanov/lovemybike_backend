package com.livemybike.shop.offers.booking;

public interface State {

    AbstractState REQUEST_STATE = new RequestState();
    AbstractState APPROVED_STATE = new ApprovedState();
    AbstractState CANCELED_STATE = new CanceledState();
    String REQUEST_STATE_STRING = "requested";
    String APPROVED_STATE_STRING = "approved";
    String CANCELED_STATE_STRING = "canceled";

    String getValue();

}
