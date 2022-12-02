package com.spring.cinemacity.model;

import javax.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer row;

    @Column
    private Integer column;

    @Column
    private Integer extraPrice;

    @ManyToOne
    @JoinColumn(name = "cinemaRom_id")
    private CinemaRoom cinemaRoom;

    public Seat() {
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

    public Integer getExtraPrice() {
        return extraPrice;
    }

    public void setExtraPrice(Integer extraPrice) {
        this.extraPrice = extraPrice;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }
}
