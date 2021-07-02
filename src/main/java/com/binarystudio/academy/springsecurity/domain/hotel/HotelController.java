package com.binarystudio.academy.springsecurity.domain.hotel;

import com.binarystudio.academy.springsecurity.domain.hotel.dto.CreateHotelDto;
import com.binarystudio.academy.springsecurity.domain.hotel.dto.HotelDto;
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
    public List<HotelDto> getHotels() {
        return hotelService.getAll();
    }

    @DeleteMapping("delete/{hotelId}")
    public void deleteHotel(@AuthenticationPrincipal User user, @PathVariable UUID hotelId) {
        hotelService.delete(user, hotelId);
    }

    @PutMapping("create")
    public HotelDto createHotel(@AuthenticationPrincipal User user, @RequestBody CreateHotelDto hotelDto) {
        return hotelService.create(user, hotelDto);
    }

    @PatchMapping("update")
    public HotelDto updateHotel(@AuthenticationPrincipal User user, @RequestBody HotelDto hotelDto) {
        return hotelService.update(user, hotelDto);
    }

    @GetMapping("{hotelId}")
    public HotelDto getHotel(@PathVariable UUID hotelId) {
        return hotelService.getById(hotelId);
    }
}
