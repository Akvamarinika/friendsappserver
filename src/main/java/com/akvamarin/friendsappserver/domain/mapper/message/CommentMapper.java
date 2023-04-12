package com.akvamarin.friendsappserver.domain.mapper.message;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.domain.entity.message.Comment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id", ignore = true)
    Comment toEntity(CommentDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "id", source = "id")
    CommentDTO toDTO(Comment entity);

    default LocalDateTime map(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    default String map(LocalDateTime value) {
        return value == null ? null : value.toString();
    }

}
