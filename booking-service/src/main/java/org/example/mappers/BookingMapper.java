package org.example.mappers;

import org.example.data.dto.BookingDtoResponse;
import org.example.data.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "userId", expression = "java(booking.getUser().getId().toString())")
    @Mapping(target = "room", expression = "java(booking.getRoom().toString())")
    BookingDtoResponse mapBookingToBookingDto(Booking booking);


}
