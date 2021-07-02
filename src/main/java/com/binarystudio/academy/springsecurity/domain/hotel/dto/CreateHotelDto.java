package com.binarystudio.academy.springsecurity.domain.hotel.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateHotelDto {
    private final String name;
    private final String description;
    private final String imageUrl;
}
