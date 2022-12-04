package com.spring.cinemacity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference(value = "order-ticket")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    @JsonBackReference(value = "seat-ticket")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "projection_id")
    @JsonBackReference(value = "projection-ticket")
    private Projection projection;

    public Ticket() {
    }

    public Long getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Projection getProjection() {
        return projection;
    }

    public void setProjection(Projection projection) {
        this.projection = projection;
    }


}
