package com.spring.cinemacity.model;

import javax.persistence.*;

@Entity
public class MovieSeat {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer row;

    @Column
    private Integer column;

    @Column
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "watchingTime_id")
    private WatchingTime watchingTime;

    public MovieSeat() {
    }

    public Long getId() {
        return id;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
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

    public WatchingTime getWatchingTime() {
        return watchingTime;
    }

    public void setWatchingTime(WatchingTime watchingTime) {
        this.watchingTime = watchingTime;
    }
}
