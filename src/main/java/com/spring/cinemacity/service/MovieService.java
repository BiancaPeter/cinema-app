package com.spring.cinemacity.service;

import com.spring.cinemacity.DTO.AddMovieDTO;
import com.spring.cinemacity.DTO.ProjectionsDTO;
import com.spring.cinemacity.model.*;
import com.spring.cinemacity.repository.CinemaRoomRepository;
import com.spring.cinemacity.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        CinemaRoom foundCinemaRoom = cinemaRoomRepository.findById(addMovieDTO.getCinemaRoomId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "the cinema was not found"));
        Movie movieToBeAdd = new Movie();
        movieToBeAdd.setMovieName(addMovieDTO.getMovieName());
        movieToBeAdd.setPrice(addMovieDTO.getPrice());
        movieToBeAdd.setCinemaRoom(foundCinemaRoom);
        Optional<Movie> foundMovie = movieRepository.findByMovieName(movieToBeAdd.getMovieName());
        if (foundMovie.isPresent()) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "This movie name already exist");
        }
        generateProjections(addMovieDTO, foundCinemaRoom, movieToBeAdd);
        return movieRepository.save(movieToBeAdd);
    }

    private void generateProjections(AddMovieDTO addMovieDTO, CinemaRoom foundCinemaRoom, Movie movieToBeAdd) {
        addMovieDTO.getDates().forEach(projectionsDTO -> {
            Optional<Projection> interferingProjection = canProjectionBeAdded(foundCinemaRoom, projectionsDTO);
            if (interferingProjection.isPresent()) {
                throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "there is already a projection between following dates" + " " + interferingProjection.get().getStartTime() + " " + interferingProjection.get().getEndTime());
            }
            Projection projection = new Projection();
            projection.setStartTime(projectionsDTO.getStartTime());
            projection.setEndTime(projectionsDTO.getEndTime());
            projection.setMovie(movieToBeAdd);
            movieToBeAdd.getProjectionList().add(projection);
            generateTicketsForProjection(foundCinemaRoom, projection);
        });
    }

    private Optional<Projection> canProjectionBeAdded(CinemaRoom foundCinemaRoom, ProjectionsDTO projection) {
        for (Movie movie : foundCinemaRoom.getMovieList()) {
            for (Projection existingProjection : movie.getProjectionList()) {
                if (!(projection.getEndTime().isBefore(existingProjection.getStartTime()) || projection.getStartTime().isAfter(existingProjection.getEndTime()))) {
                    return Optional.of(existingProjection);
                }
            }
        }
        return Optional.empty();
    }

    private void generateTicketsForProjection(CinemaRoom foundCinemaRoom, Projection projection) {
        for (Seat seat : foundCinemaRoom.getSeatList()) {
            Ticket ticket = new Ticket();
            ticket.setAvailable(true);
            ticket.setProjection(projection);
            projection.getTicketList().add(ticket);
            ticket.setSeat(seat);
        }
    }

    public List<Projection> getAllProjectionsAvailable(Long movieId) {
        Movie foundMovie = movieRepository.findById(movieId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "the movie was not found"));
//        List<Projection> projectionsAvailable = new ArrayList<>();
//        for (Projection projection : foundMovie.getProjectionList()) {
//            if (projection.getStartTime().isAfter(LocalDateTime.now())) {
//                boolean hasProjectionAvailableTickets = projection.getTicketList().stream()
//                        .anyMatch(ticket -> ticket.getAvailable().equals(true));
//                if (hasProjectionAvailableTickets){
//                    projectionsAvailable.add(projection);
//                }
//            }
//        }
//        return projectionsAvailable;

        return foundMovie.getProjectionList().stream()
                .filter(projection -> projection.getStartTime().isAfter(LocalDateTime.now()))
                .filter(projection -> hasProjectionAvailableTickets(projection))
                .collect(Collectors.toList());
    }

    public boolean hasProjectionAvailableTickets(Projection projection) {
        return projection.getTicketList().stream()
                .anyMatch(ticket -> ticket.getAvailable());
    }
}
