package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import com.binarystudio.academy.springsecurity.domain.user.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("hotels")
public class HotelController {
	private final HotelService hotelService;

	public HotelController(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	@GetMapping("all")
	public List<Hotel> getHotels() {
		return hotelService.getAll();
	}

	@DeleteMapping("delete/{hotelId}")
	public void deleteHotel(@AuthenticationPrincipal User user, @PathVariable UUID hotelId) {
		hotelService.delete(user, hotelId);
	}

	@PutMapping("create")
	public Hotel createHotel(@RequestBody Hotel hotel) {
		return hotelService.create(hotel);
	}

	@PatchMapping("update")
	public Hotel updateHotel(@AuthenticationPrincipal User user, @RequestBody Hotel hotel) {
		return hotelService.update(user, hotel);
	}

	@GetMapping("{hotelId}")
	public Hotel getHotel(@PathVariable UUID hotelId) {
		return hotelService.getById(hotelId);
	}
}
