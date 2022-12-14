package com.spring.cinemacity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.spring.cinemacity.DTO.AddMovieDTO;
import com.spring.cinemacity.DTO.ProjectionsDTO;
import com.spring.cinemacity.model.*;
import com.spring.cinemacity.repository.CinemaRoomRepository;
import com.spring.cinemacity.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    private CinemaRoomRepository cinemaRoomRepository;
    private static final String FIND_MOVIE_BY_NAME_URL = "https://api.themoviedb.org/3/search/movie?api_key={APIkey}&language=en-US&query={moviename}&page=1&include_adult=false";

    private static final String FIND_MOVIE_DETAILS_BY_ID_URL = "https://api.themoviedb.org/3/movie/{movieId}?api_key={APIkey}&language=en-US";
    @Value("${api.themoviedb.key}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public MovieService(MovieRepository movieRepository, CinemaRoomRepository cinemaRoomRepository, RestTemplate restTemplate) {
        this.movieRepository = movieRepository;
        this.cinemaRoomRepository = cinemaRoomRepository;
        this.restTemplate = restTemplate;
    }

    public Movie addMovie(AddMovieDTO addMovieDTO) throws JsonProcessingException {
        CinemaRoom foundCinemaRoom = cinemaRoomRepository.findById(addMovieDTO.getCinemaRoomId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "the cinema was not found"));
        Optional<Movie> foundMovie = movieRepository.findByMovieName(addMovieDTO.getMovieName());
        if (foundMovie.isPresent()) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "This movie name already exist");
        }
        Movie movieToBeAdd = new Movie();
        movieToBeAdd.setPrice(addMovieDTO.getPrice());
        movieToBeAdd.setCinemaRoom(foundCinemaRoom);
        JsonNode responseMovieBody = getResponseBodyJson(FIND_MOVIE_BY_NAME_URL, addMovieDTO.getMovieName());
        addMovieDetails(movieToBeAdd, responseMovieBody);
        JsonNode responseMovieDetailsBody = getResponseBodyJson(FIND_MOVIE_DETAILS_BY_ID_URL, responseMovieBody.path("results").get(0).path("id").asInt());
        addMovieGenres(movieToBeAdd, responseMovieDetailsBody);
        generateProjections(addMovieDTO, foundCinemaRoom, movieToBeAdd);
        return movieRepository.save(movieToBeAdd);
    }

    private void addMovieGenres(Movie movieToBeAdd, JsonNode responseMovieDetailsBody) {
        List<String> genres = new ArrayList<>();
        ArrayNode genresJson = (ArrayNode) responseMovieDetailsBody.path("genres");
        for (JsonNode genreJson : genresJson) {
            genres.add(genreJson.path("name").asText());
        }
        movieToBeAdd.setGenres(genres);
    }

    private void addMovieDetails(Movie movieToBeAdd, JsonNode responseMovieBody) {
        if (responseMovieBody.path("results").isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't add a movie with this name");
        }
        JsonNode firstResult = responseMovieBody.path("results").get(0);
        String releaseDateText = firstResult.path("release_date").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = LocalDate.parse(releaseDateText, formatter);
        movieToBeAdd.setOverview(firstResult.path("overview").asText());
        movieToBeAdd.setLanguage(firstResult.path("original_language").asText());
        movieToBeAdd.setVoteAverage(firstResult.path("vote_average").asDouble());
        movieToBeAdd.setReleaseDate(releaseDate);
        movieToBeAdd.setMovieName(firstResult.path("original_title").asText());
    }

    public JsonNode getResponseBodyJson(String requestBaseUrl, String movieName) throws JsonProcessingException {
        URI url = new UriTemplate(requestBaseUrl).expand(apiKey, movieName);
        return makeAPICall(url);
    }

    public JsonNode getResponseBodyJson(String requestBaseUrl, Integer movieId) throws JsonProcessingException {
        URI url = new UriTemplate(requestBaseUrl).expand(movieId, apiKey);
        return makeAPICall(url);
    }

    private JsonNode makeAPICall(URI url) throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.getBody());
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
