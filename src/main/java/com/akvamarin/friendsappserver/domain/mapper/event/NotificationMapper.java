package com.akvamarin.friendsappserver.domain.mapper.event;

import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = {EventMapper.class, UserMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class NotificationMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "eventName", source = "event.name")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userNickname", source = "user.nickname")
    @Mapping(target = "userUrlAvatar", source = "user.urlAvatar")
    @Mapping(target = "forOwner", ignore = true)
    public abstract ViewNotificationDTO toDTO(NotificationParticipant entity);
}


