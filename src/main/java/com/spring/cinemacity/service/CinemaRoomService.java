package com.spring.cinemacity.service;

import com.spring.cinemacity.DTO.AddCinemaRoomDTO;
import com.spring.cinemacity.DTO.ExtraPriceDTO;
import com.spring.cinemacity.model.CinemaRoom;
import com.spring.cinemacity.model.Seat;
import com.spring.cinemacity.repository.CinemaRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CinemaRoomService {
    private CinemaRoomRepository cinemaRoomRepository;

    @Autowired
    public CinemaRoomService(CinemaRoomRepository cinemaRoomRepository) {
        this.cinemaRoomRepository = cinemaRoomRepository;
    }

    public CinemaRoom addCinemaRoom(AddCinemaRoomDTO addCinemaRoomDTO) {
        CinemaRoom cinemaRoom = new CinemaRoom();
        cinemaRoom.setNumberOfRows(addCinemaRoomDTO.getNumberOfRows());
        cinemaRoom.setNumberOfCols(addCinemaRoomDTO.getNumberOfCols());
        generateSeatsForCinemaRoom(addCinemaRoomDTO, cinemaRoom);
        generateExtraPricesForCinemaRoom(addCinemaRoomDTO, cinemaRoom);
        return cinemaRoomRepository.save(cinemaRoom);
    }

    private void generateExtraPricesForCinemaRoom(AddCinemaRoomDTO addCinemaRoomDTO, CinemaRoom cinemaRoom) {
        for (ExtraPriceDTO extraPriceDTO : addCinemaRoomDTO.getExtraPrices()) {
            for (int i = extraPriceDTO.getStartingRow(); i <= extraPriceDTO.getEndingRow(); i++) {
                for (int j = 1; j <= addCinemaRoomDTO.getNumberOfCols(); j++) {
                    Seat seat = getSeatByRowAndColumn(cinemaRoom, i, j).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "the seat was not found"));
                    seat.setExtraPrice(extraPriceDTO.getExtraPrice());
                }
            }
        }
    }

    private static void generateSeatsForCinemaRoom(AddCinemaRoomDTO addCinemaRoomDTO, CinemaRoom cinemaRoom) {
        for (int i = 1; i <= addCinemaRoomDTO.getNumberOfRows(); i++) {
            for (int j = 1; j <= addCinemaRoomDTO.getNumberOfCols(); j++) {
                Seat seat = new Seat();
                seat.setSeatRow(i);
                seat.setSeatCol(j);
                seat.setExtraPrice(0);
                cinemaRoom.getSeatList().add(seat);
                seat.setCinemaRoom(cinemaRoom);
            }
        }
    }

    public Optional<Seat> getSeatByRowAndColumn(CinemaRoom cinemaRoom, Integer row, Integer column) {
//        for (Seat seat : cinemaRoom.getSeatList()) {
//            if (seat.getSeatRow() == row && seat.getSeatColumn() == column) {
//                return seat;
//            }
//        }
//        return null;
        return cinemaRoom.getSeatList().stream()
                .filter(seat -> seat.getSeatRow() == row && seat.getSeatCol() == column)
                .findFirst();
    }

}
