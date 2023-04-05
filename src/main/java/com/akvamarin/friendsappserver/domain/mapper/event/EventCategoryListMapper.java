package com.akvamarin.friendsappserver.domain.mapper.event;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventCategoryMapper.class)
public interface EventCategoryListMapper {

    List<EventCategoryDTO> toDTOList(List<EventCategory> entities);

    List<EventCategory> toEntityList(List<EventCategoryDTO> dtos);
}
