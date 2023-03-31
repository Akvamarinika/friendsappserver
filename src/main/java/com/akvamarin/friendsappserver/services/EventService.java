package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.EventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface EventService {
    EventDTO createNewEvent(@NotNull EventDTO eventDTO);
    List<Event> saveAll(List<EventDTO> eventDTOs);
    List<EventDTO> findAll();
    EventDTO findById(long eventId);
    EventDTO updateEvent(@NonNull EventDTO eventDTO);
    boolean deleteById(long eventId);
    void deleteAll();
}
