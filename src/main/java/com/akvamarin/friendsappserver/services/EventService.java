package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventUpdateDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface EventService {
    Event createNewEvent(@NotNull EventDTO eventDTO);
    List<Event> saveAll(List<EventDTO> eventDTOs);
    List<ViewEventDTO> findAll();
    ViewEventDTO findById(long eventId);

    ViewEventUpdateDTO findByIdForViewUpdate(long eventId);

    ViewEventDTO updateEvent(@NonNull EventDTO eventDTO);
    boolean deleteById(long eventId);
    void deleteAll();
}
