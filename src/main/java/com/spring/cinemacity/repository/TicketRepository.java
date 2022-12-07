package com.spring.cinemacity.repository;

import com.spring.cinemacity.model.Projection;
import com.spring.cinemacity.model.Seat;
import com.spring.cinemacity.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    Ticket findByProjectionAndSeat(Projection projection, Seat seat);
}