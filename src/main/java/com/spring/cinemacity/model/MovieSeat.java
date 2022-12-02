package com.spring.cinemacity.model;

import javax.persistence.*;

@Entity
public class MovieSeat {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer numberOfRows;

    @Column
    private Integer numberOfColumns;

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

    public Integer getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(Integer numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public Integer getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(Integer numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
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
