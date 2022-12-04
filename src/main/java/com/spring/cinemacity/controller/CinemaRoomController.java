package com.spring.cinemacity.controller;

import com.spring.cinemacity.DTO.AddCinemaRoomDTO;
import com.spring.cinemacity.model.CinemaRoom;
import com.spring.cinemacity.service.CinemaRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema")
public class CinemaRoomController {

    private CinemaRoomService cinemaRoomService;

    @Autowired
    public CinemaRoomController(CinemaRoomService cinemaRoomService) {
        this.cinemaRoomService = cinemaRoomService;
    }

    @PostMapping("/add")
    private CinemaRoom addCinemaRoom(@RequestBody AddCinemaRoomDTO addCinemaRoomDTO){
        return cinemaRoomService.addCinemaRoom(addCinemaRoomDTO);
    }
}
