package org.example.mappers;

import org.example.data.dto.UserDto;
import org.example.data.model.User;
import org.example.data.model.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface UserMapper {
    @Mapping(target = "role", source = "stringRole", qualifiedByName = "mapRole")
    User userDtoToUser(UserDto dto);

    UserDto userToUserDto(User user);


    @Named("mapRole")
    default RoleType mapToRole(String stringRole) {

        return RoleType.valueOf(stringRole);
    }
}
