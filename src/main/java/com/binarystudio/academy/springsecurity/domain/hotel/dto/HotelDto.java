package com.binarystudio.academy.springsecurity.domain.hotel.dto;

import com.binarystudio.academy.springsecurity.domain.hotel.model.Hotel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class HotelDto {
    private final UUID id;
    private final String name;
    private final String description;
    private final String imageUrl;

    public static HotelDto fromEntity(Hotel hotel) {
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .description(hotel.getDescription())
                .imageUrl(hotel.getImageUrl())
                .build();
    }
}
