package org.example.data.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RoomDtoResponse {

    private Long Id;

    private String roomName;

    private String description;

    private Integer number;

    private Float price;

    private Integer population;

    private List<LocalDate> closedDates;

    private String hotelName;
}
