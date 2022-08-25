package cz.inqool.dl4dh.krameriusplus.core.system.user.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.mapper.DatedObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements DatedObjectMapper<User, UserCreateDto, UserDto> {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Override
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userCreateDto.getPassword()))")
    public abstract User fromCreateDto(UserCreateDto userCreateDto);

    @Override
    @Mapping(target = "authorities", ignore = true)
    public abstract UserDto toDto(User user);

    @Override
    @Mapping(target = "authorities", ignore = true)
    public abstract User fromDto(UserDto userDto);
}
