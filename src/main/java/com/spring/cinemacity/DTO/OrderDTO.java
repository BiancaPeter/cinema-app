package com.spring.cinemacity.DTO;

import java.util.List;

public class OrderDTO {

    private long projectionId;
    private List<SeatDTo> seats;

    public OrderDTO(long projectionId, List<SeatDTo> seats) {
        this.projectionId = projectionId;
        this.seats = seats;
    }

    public long getProjectionId() {
        return projectionId;
    }

    public void setProjectionId(long projectionId) {
        this.projectionId = projectionId;
    }

    public List<SeatDTo> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatDTo> seats) {
        this.seats = seats;
    }
}

