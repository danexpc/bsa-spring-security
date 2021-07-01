package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import com.binarystudio.academy.springsecurity.domain.user.model.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class HotelService {
	private final HotelRepository hotelRepository;

	public HotelService(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	public void delete(UUID hotelId) {
		// 4. todo: only the owner of the hotel or admin may delete the hotel
		boolean wasDeleted = hotelRepository.delete(hotelId);
		if (!wasDeleted) {
			throw new NoSuchElementException();
		}
	}

	public List<Hotel> getAll() {
		return hotelRepository.getHotels();
	}


	public Hotel update(User user, Hotel hotel) {
		getById(hotel.getId());

		if (doesUserHavePermission(user, hotel)) {
			return hotelRepository.save(hotel);
		}

		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Have no rights to update hotel");
	}

	public Hotel create(Hotel hotel) {
		return hotelRepository.save(hotel);
	}

	public Hotel getById(UUID hotelId) {
		return hotelRepository.getById(hotelId).orElseThrow();
	}

	private boolean doesUserHavePermission(User user, Hotel hotel) {
		return user.getAuthorities().contains(UserRole.ADMIN) ||
				hotel.getOwnerId().equals(user.getId());
	}
}
