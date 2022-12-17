package com.spring.cinemacity.repository;

import com.spring.cinemacity.model.CinemaRoom;
import com.spring.cinemacity.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    Seat findBySeatRowAndSeatColAndCinemaRoom(Integer row, Integer col, CinemaRoom cinemaRoom);

}
