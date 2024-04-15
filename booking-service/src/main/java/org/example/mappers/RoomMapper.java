package org.example.mappers;

import org.example.data.dto.RoomDtoRequest;
import org.example.data.dto.RoomDtoResponse;
import org.example.data.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room mapRoomDtoToRoom(RoomDtoRequest dto);

    @Mapping(target = "hotelName", expression = "java(room.getHotel().getHotelName())")
    RoomDtoResponse mapRoomToRoomDto(Room room);


}
