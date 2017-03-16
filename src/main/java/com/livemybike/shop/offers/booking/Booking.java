package com.livemybike.shop.offers.booking;

import com.livemybike.shop.security.Account;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking {

    private AbstractState state;
    private Date from;
    private Date to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account requestedBy;

    public State getState() {
        return state;
    }

    void setState(AbstractState state) {
        this.state = state;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Account getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Account requestedBy) {
        this.requestedBy = requestedBy;
    }

    public void request() throws InvalidStateTransitionException {
        state.request(this);
    }

    public void approve() throws InvalidStateTransitionException {
        state.approve(this);
    }

    public void cancel() throws InvalidStateTransitionException {
        state.cancel(this);
    }

    public void reopen() throws InvalidStateTransitionException {
        state.reopen(this);
    }

}
