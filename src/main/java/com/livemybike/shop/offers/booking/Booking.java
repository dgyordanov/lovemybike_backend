package com.livemybike.shop.offers.booking;

import com.livemybike.shop.offers.Offer;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.util.DateUtil;
import org.apache.commons.lang.Validate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "bookings")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Convert(converter = StateJpaConverter.class)
    private AbstractState state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fromDate")
    private Date fromDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by_id")
    private Account requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;

    public Booking(Date from, Date to, Offer offer, Account requestedBy) {
        Validate.notNull(from, "from date can not be null");
        Validate.notNull(to, "to date can not be null");
        Validate.notNull(offer, "offer can not be null");
        Validate.notNull(requestedBy, "requestedBy can not be null");

        Date today = DateUtil.today();
        from = DateUtil.getBeginningOfDay(from);
        to = DateUtil.getEndOfDay(to);

        if (from.after(to)) {
            throw new IllegalArgumentException("From date could not be after to date");
        }

        if (from.before(today)) {
            throw new IllegalArgumentException("From date could not be before today");
        }

        this.fromDate = from;
        this.to = to;
        this.offer = offer;
        this.requestedBy = requestedBy;
        this.state = State.REQUEST_STATE;
    }

    Booking() {
        this.state = State.REQUEST_STATE;
    }

    public State getState() {
        return state;
    }

    void setState(AbstractState state) {
        this.state = state;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getTo() {
        return to;
    }

    public Account getRequestedBy() {
        return requestedBy;
    }

    public Offer getOffer() {
        return offer;
    }

    public Long getId() {
        return id;
    }

    void approve(Account currentUser) throws InvalidStateTransitionException {
        state.approve(this, currentUser);
    }

    void cancel(Account currentUser) throws InvalidStateTransitionException {
        state.cancel(this, currentUser);
    }

    void reopen(Account currentUser) throws InvalidStateTransitionException {
        state.reopen(this, currentUser);
    }

}
