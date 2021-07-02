package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.dto.CreateHotelDto;
import com.binarystudio.academy.springsecurity.domain.hotel.dto.HotelDto;
import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import com.binarystudio.academy.springsecurity.exceptions.HotelNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public void delete(User user, UUID hotelId) {
        var wasDeleted = false;

        var hotel = hotelRepository.getById(hotelId);

        if (doesUserHavePermission(user, hotel.orElseThrow(
                () -> new NoSuchElementException("Not found")).getOwnerId())
        ) {
            wasDeleted = hotelRepository.delete(hotelId);
        }

        if (!wasDeleted) {
            throw new NoSuchElementException();
        }
    }

    public List<HotelDto> getAll() {
        return hotelRepository
                .getHotels()
                .stream()
                .map(HotelDto::fromEntity)
                .collect(Collectors.toList());
    }

    public HotelDto update(User user, HotelDto hotelDto) {
        var hotel = hotelRepository
                .getById(hotelDto.getId())
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found"));

        log.error(user);
        log.error(hotel);
        if (doesUserHavePermission(user, hotel.getOwnerId())) {
            return HotelDto.fromEntity(hotelRepository.update(
                    Hotel.of(
                            hotel.getId(),
                            hotelDto.getName(),
                            hotelDto.getDescription(),
                            hotelDto.getImageUrl(),
                            hotel.getOwnerId()
                    )
            )
                    .orElseThrow(() -> new HotelNotFoundException("Hotel not found")));
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Have no rights to update hotel");
    }

    public HotelDto create(User user, CreateHotelDto hotelDto) {
        var hotel = Hotel.of(
                hotelDto.getName(),
                hotelDto.getDescription(),
                hotelDto.getImageUrl(),
                user.getId()
        );

        hotelRepository.save(hotel);
        return HotelDto.fromEntity(hotel);
    }

    public HotelDto getById(UUID hotelId) {
        return HotelDto.fromEntity(hotelRepository.getById(hotelId).orElseThrow(() -> new HotelNotFoundException("Hotel not found")));
    }

    private boolean doesUserHavePermission(User user, UUID ownerId) {
        return user.getAuthorities().contains(UserRole.ADMIN) ||
                ownerId.equals(user.getId());
    }
}
