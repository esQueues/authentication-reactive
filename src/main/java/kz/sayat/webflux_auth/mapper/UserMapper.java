package kz.sayat.webflux_auth.mapper;

import kz.sayat.webflux_auth.dto.UserDto;
import kz.sayat.webflux_auth.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);

}
