package com.akvamarin.friendsappserver.domain.mapper;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventCategoryMapper {

    EventCategoryDTO toDTO(EventCategory eventCategory);

    EventCategory toEntity(EventCategoryDTO eventCategoryDTO);

    void updateEntity(EventCategoryDTO eventCategoryDTO, @MappingTarget EventCategory eventCategory);
}

