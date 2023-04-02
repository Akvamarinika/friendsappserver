package com.akvamarin.friendsappserver.domain.mapper.event;

import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = EventCategoryMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface EventMapper {


   // @Mapping(source = "eventCategory.name", target = "categoryName")
    //@Mapping(source = "eventCategories", target = "eventCategoryList", qualifiedByName = "toDTOList")

    @Mapping(source = "eventCategory.id", target = "eventCategoryId")
    @Mapping(source = "user.id", target = "ownerId")
    EventDTO toDTO(Event event);

    @InheritInverseConfiguration
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "id", target = "id", ignore = true)
    //@Mapping(source = "eventCategories", target = "ownerId", ignore = true)
    Event toEntity(EventDTO eventDTO);

    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(EventDTO eventDTO, @MappingTarget Event event);



}