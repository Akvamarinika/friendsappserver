package com.akvamarin.friendsappserver.domain.mapper;

import com.akvamarin.friendsappserver.domain.dto.AuthUserSocialDTO;
import com.akvamarin.friendsappserver.domain.dto.request.UserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.enums.Role;
import com.akvamarin.friendsappserver.domain.mapper.location.CityMapper;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = {CityMapper.class})
public abstract class UserMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "roles", source = "authorities")
    @Mapping(target = "cityDTO", source = "city")
    public abstract ViewUserDTO toDTO(User user);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "password", source = "password", ignore = true)
    @Mapping(target = "id", source = "id", ignore = true)   //No ID, when registering
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "roles", target = "authorities", qualifiedByName = "stringToRole")
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract User toEntity(UserDTO dto);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "authorities", source = "roles", qualifiedByName = "stringToRole")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "city", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract void updateEntity(UserDTO dto, @MappingTarget User user); // @MappingTarget - обновляет переданный объект

    //AuthUserSocialDTO
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", ignore = true) // no
    @Mapping(target = "dateOfBirthday", source = "dateOfBirth", dateFormat = "d.M.yyyy") //yyyy-MM-dd
    @Mapping(target = "nickname", source = "firstName")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "aboutMe", ignore = true) // *
    @Mapping(target = "smoking", ignore = true) // *
    @Mapping(target = "alcohol", ignore = true) // *
    @Mapping(target = "psychotype", ignore = true) // *
    @Mapping(target = "urlAvatar", source = "photo")
    @Mapping(target = "city", ignore = true)  // add from city service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "authorities", source = "roles", qualifiedByName = "stringToRole") // default
    public abstract User toEntity(AuthUserSocialDTO socialDTO);

    /* @Mapping(target = "authorities", source = "roles", qualifiedByName = "stringToRole")
    @Mapping(target = "city", ignore = true)  // add from city service
    public abstract void updateEntity(AuthUserSocialDTO socialDTO, @MappingTarget User user); */

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "cityDTO", source = "city")
    @Mapping(target = "roles", source = "authorities")
    public abstract ViewUserSlimDTO userToViewUserSlimDTO(User user);

    @Named("stringToRole")
    protected Set<Role> stringToRole(Set<String> authorities) {
        if (authorities != null) {
            return authorities.stream()
                    .map(Role::valueOf)
                    .collect(toSet());
        }
        return  Collections.emptySet();
    }

}
