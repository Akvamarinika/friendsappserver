package com.akvamarin.friendsappserver.domain.mapper.event;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventCategoryMapper {

    EventCategoryDTO toDTO(EventCategory eventCategory);

    @Mapping(target = "id", source = "id", ignore = true)
    EventCategory toEntity(EventCategoryDTO eventCategoryDTO);

    void updateEntity(EventCategoryDTO eventCategoryDTO, @MappingTarget EventCategory eventCategory);
}

