package com.spring.cinemacity.controller;

import com.spring.cinemacity.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/totalPrice/{day}")
    public Long getValueOfTicketsSoldByDay(@PathVariable ("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
        return ticketService.getValueOfTicketsSoldByDay(day);
    }

    @GetMapping("/totalPrice/{day}/{movieName}")
    public Long getValueOfTicketsSoldByDayAndMovieName(@PathVariable ("day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,@PathVariable String movieName) {
        return ticketService.getValueOfTicketsSoldByDayAndMovieName(day,movieName);
    }

    @GetMapping("/totalTickets/{day}")
    public Integer getNumberOfTicketsSoldByDay(@PathVariable LocalDate day) {
        return ticketService.getNumberOfTicketsSoldByDay(day);
    }

    @GetMapping("/totalTickets/{day}/{movieName}")
    public Integer getNumberOfTicketsSoldByDayAndMovieName(@PathVariable LocalDate day,@PathVariable String movieName) {
        return ticketService.getNumberOfTicketsSoldByDayAndMovieName(day,movieName);
    }
}
