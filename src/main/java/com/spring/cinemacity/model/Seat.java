package com.spring.cinemacity.model;

import javax.persistence.*;

@Entity
public class Seat {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer numberOfRows;

    @Column
    private Integer numberOfColumns;

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
