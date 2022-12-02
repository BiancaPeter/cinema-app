package com.spring.cinemacity.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class CinemaRoom {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer numberOfRows;

    @Column
    private Integer numberOfCols;

    @Column
    private Integer extraPrices;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL)
    private List<Movie> movieList;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL)
    private List<Seat> seatList;

    public CinemaRoom() {
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

    public Integer getNumberOfCols() {
        return numberOfCols;
    }

    public void setNumberOfCols(Integer numberOfCols) {
        this.numberOfCols = numberOfCols;
    }

    public Integer getExtraPrices() {
        return extraPrices;
    }

    public void setExtraPrices(Integer extraPrices) {
        this.extraPrices = extraPrices;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }
}
