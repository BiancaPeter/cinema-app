package com.spring.cinemacity.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class WatchingTime {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @OneToMany(mappedBy = "watchingTime", cascade = CascadeType.ALL)
    private List<MovieSeat>movieSeatList;

    public WatchingTime() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<MovieSeat> getMovieSeatList() {
        return movieSeatList;
    }

    public void setMovieSeatList(List<MovieSeat> movieSeatList) {
        this.movieSeatList = movieSeatList;
    }
}
