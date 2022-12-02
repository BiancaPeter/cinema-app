package com.spring.cinemacity.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;

    private String movieName;

    private Integer price;

    @ManyToOne
    @JoinColumn(name = "cinemaRoom_id")
    private CinemaRoom cinemaRoom;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    private List<WatchingTime> watchingTimeList;

    public Movie() {
    }

    public Long getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    public List<WatchingTime> getDateList() {
        return watchingTimeList;
    }

    public void setDateList(List<WatchingTime> watchingTimeList) {
        this.watchingTimeList = watchingTimeList;
    }

    public List<WatchingTime> getWatchingTimeList() {
        return watchingTimeList;
    }

    public void setWatchingTimeList(List<WatchingTime> watchingTimeList) {
        this.watchingTimeList = watchingTimeList;
    }
}
