package com.akvamarin.friendsappserver.domain.mapper;
import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.enums.Role;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "authorities", target = "roles")
    public abstract UserDTO toDTO(User user);

    @InheritInverseConfiguration
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "password", source = "password", ignore = true)
    @Mapping(target = "id", source = "id", ignore = true)   //No ID, when registering
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "roles", target = "authorities", qualifiedByName = "stringToRole")
    public abstract User toEntity(UserDTO dto);

    @Mapping(source = "roles", target = "authorities", qualifiedByName = "stringToRole")
    public abstract void updateEntity(UserDTO dto, @MappingTarget User user); // @MappingTarget - обновляет переданный объект

    //public abstract AuthUserSocialDTO toDTO(User user);
    @Mapping(target = "email", source = "username")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "dateOfBirthday", ignore = true)
    @Mapping(target = "nickname", ignore = true)
    @Mapping(target = "sex", ignore = true)
    @Mapping(target = "aboutMe", ignore = true)
    @Mapping(target = "smoking", ignore = true)
    @Mapping(target = "alcohol", ignore = true)
    @Mapping(target = "psychotype", ignore = true)
    @Mapping(target = "urlAvatar", ignore = true)
    @Mapping(target = "city", source = "city", qualifiedByName = "stringToCity")
    @Mapping(target = "country", source = "country", qualifiedByName = "stringToCountry")
    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(AuthUserSocialDTO socialDTO);

    @Mapping(target = "username", source = "email")
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "socialToken", ignore = true)
    @Mapping(target = "vkId", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = , ignore = true)
    @Mapping(target = , ignore = true)
    @Mapping(target = "sex", ignore = true)

    @Named("stringToRole")
    protected Set<Role> stringToRole(Set<String> authorities) {
        if (authorities != null) {
            return authorities.stream()
                    .map(Role::valueOf)
                    .collect(toSet());
        }
        return  Collections.emptySet();
    }

    @Named("stringToCity")
    protected City stringToCity(String city) {
        if (authorities != null) {
            return authorities.stream()
                    .map(Role::valueOf)
                    .collect(toSet());
        }
        return  Collections.emptySet();
    }
}
