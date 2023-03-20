package com.akvamarin.friendsappserver.domain.mapper;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
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
    @Mapping(target = "passwordConfirm", ignore = true)
    @Mapping(target = "passwordToChange", ignore = true)
    @Mapping(source = "authorities", target = "roles")
    public abstract UserDTO toDTO(User user);


    @InheritInverseConfiguration
    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)   //No ID, when registering
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "roles", target = "authorities", qualifiedByName = "stringToRole")
    public abstract User toEntity(UserDTO dto);

    @Mapping(source = "roles", target = "authorities", qualifiedByName = "stringToRole")
    public abstract void updateEntity(UserDTO dto, @MappingTarget User user);

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
