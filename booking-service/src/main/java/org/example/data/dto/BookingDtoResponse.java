package org.example.data.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class BookingDtoResponse {

    Long id;

    LocalDate arrivalDate;

    LocalDate leavingDate;

    String room;

    String userId;
}

