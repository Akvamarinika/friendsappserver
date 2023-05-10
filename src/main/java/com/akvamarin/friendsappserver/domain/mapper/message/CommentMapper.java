package com.akvamarin.friendsappserver.domain.mapper.message;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewCommentDTO;
import com.akvamarin.friendsappserver.domain.entity.message.Comment;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import org.mapstruct.*;

import java.time.LocalDateTime;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id", ignore = true)
    Comment toEntity(CommentDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "id", source = "id")
    CommentDTO toDTO(Comment entity);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "userSlimDTO", source = "user")
    @Mapping(target = "userSlimDTO.roles", source = "user.authorities")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "id", source = "id")
    ViewCommentDTO toViewDTO(Comment entity);

    default LocalDateTime map(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    default String map(LocalDateTime value) {
        return value == null ? null : value.toString();
    }

}
