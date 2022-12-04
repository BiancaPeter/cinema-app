package com.spring.cinemacity.service;

import com.spring.cinemacity.DTO.AddMovieDTO;
import com.spring.cinemacity.model.*;
import com.spring.cinemacity.repository.CinemaRoomRepository;
import com.spring.cinemacity.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    private CinemaRoomRepository cinemaRoomRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, CinemaRoomRepository cinemaRoomRepository) {
        this.movieRepository = movieRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    public Movie addMovie(AddMovieDTO addMovieDTO) {
        Movie movie = new Movie();
        movie.setMovieName(addMovieDTO.getMovieName());
        CinemaRoom foundCinemaRoom = cinemaRoomRepository.findById(addMovieDTO.getCinemaRoomId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "the cinema was not found"));
        movie.setCinemaRoom(foundCinemaRoom);
        addMovieDTO.getDates().forEach(projectionsDTO -> {
            Projection projection = new Projection();
            projection.setStartTime(projectionsDTO.getStartTime());
            projection.setEndTime(projectionsDTO.getEndTime());
            projection.setMovie(movie);
            movie.getProjectionList().add(projection);

            for (Seat seat: foundCinemaRoom.getSeatList() ) {
                Ticket ticket = new Ticket();
                ticket.setAvailable(true);
                ticket.setProjection(projection);
                projection.getTicketList().add(ticket);
                ticket.setSeat(seat);
            }
        });
        return movieRepository.save(movie);
    }
}
