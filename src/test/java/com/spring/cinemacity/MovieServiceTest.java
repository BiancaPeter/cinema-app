package com.spring.cinemacity;

import com.spring.cinemacity.DTO.AddMovieDTO;
import com.spring.cinemacity.model.CinemaRoom;
import com.spring.cinemacity.model.Movie;
import com.spring.cinemacity.repository.CinemaRoomRepository;
import com.spring.cinemacity.repository.MovieRepository;
import com.spring.cinemacity.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CinemaRoomRepository cinemaRoomRepository;


    @Test
    void testAddMovie_ShouldTrowExceptoin(){
        //given

        AddMovieDTO addMovieDTO = new AddMovieDTO("Ajunul Craciunului",null,0,null);

        //when
        when(cinemaRoomRepository.findById(any())).thenReturn(Optional.of(new CinemaRoom()));
        when(movieRepository.findByMovieName(addMovieDTO.getMovieName())).thenReturn(Optional.of(new Movie(null,addMovieDTO.getMovieName(),0,null,null,null,null,null,null)));

        assertThrows(ResponseStatusException.class, () -> movieService.addMovie(addMovieDTO));
    }
}

