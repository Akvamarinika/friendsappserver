package com.akvamarin.friendsappserver.domain.mapper;

import com.akvamarin.friendsappserver.domain.dto.EventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDTO toDTO(Event event);
    Event toEntity(EventDTO eventDTO);
    void updateEntity(EventDTO eventDTO, @MappingTarget Event event);
}