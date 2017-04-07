package com.livemybike.shop.offers.booking;

import java.util.Date;

public class BookingDTO {

    private Long id;
    private Date fromDate;
    private Date toDate;
    private Long requestedById;
    private Long offerId;

    public BookingDTO() {}

    public BookingDTO(Long id, Date fromDate, Date toDate, Long requestedById, Long offerId) {
        this.id = id;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.requestedById = requestedById;
        this.offerId = offerId;
    }

    public Long getId() {
        return id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Long getRequestedById() {
        return requestedById;
    }

    public Long getOfferId() {
        return offerId;
    }

}
