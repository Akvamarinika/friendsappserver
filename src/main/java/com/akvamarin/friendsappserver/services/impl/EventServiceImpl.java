package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventUpdateDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.mapper.event.EventCategoryListMapper;
import com.akvamarin.friendsappserver.domain.mapper.event.EventMapper;
import com.akvamarin.friendsappserver.repositories.EventCategoryRepository;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventCategoryRepository categoryRepository;

    private final EventCategoryListMapper eventCategoryListMapper;

    private final UserRepository userRepository;


    @Transactional
    @Override
    public Event createNewEvent(@NotNull EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);

        EventCategory eventCategory = categoryRepository.findById(eventDTO.getEventCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        User userOwner = userRepository.findById(eventDTO.getOwnerId())
                .orElseThrow(EntityNotFoundException::new);

        event.setEventCategory(eventCategory);
        event.setUser(userOwner);
        return eventRepository.save(event);
    }

    @Transactional
    @Override
    public List<Event> saveAll(List<EventDTO> eventDTOs) {
        List<Event> events = eventDTOs.stream()
                .map(eventMapper::toEntity)
                .collect(Collectors.toList());
        return eventRepository.saveAll(events);
    }

    @Transactional
    @Override
    public List<ViewEventDTO> findAll() {
        List<ViewEventDTO> result = eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : EventDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public ViewEventDTO findById(long eventId) {
        ViewEventDTO result = eventRepository.findById(eventId)
                .map(eventMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventId)));
        log.info("Method *** findById *** : EventDTO = {} EventID = {}", result, eventId);
        return result;
    }

    @Transactional
    @Override
    public ViewEventUpdateDTO findByIdForViewUpdate(long eventId) {
        List<EventCategory> categories = categoryRepository.findAll();
        categories.sort(Comparator.comparing(EventCategory::getName));

        ViewEventUpdateDTO result = eventRepository.findById(eventId)
                .map(eventMapper::toUpdateDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventId)));

        List<EventCategoryDTO> categoryDTOs = eventCategoryListMapper.toDTOList(categories);
        result.setEventCategories(categoryDTOs);
        log.info("Method *** findByIdForViewUpdate *** : EventDTO = {} EventID = {}", result, eventId);
        return result;
    }

    @Override
    @Transactional
    public ViewEventDTO updateEvent(@NonNull EventDTO eventDTO) {
        Event event = eventRepository.findById(eventDTO.getId())
                .map(ev -> {
                    eventMapper.updateEntity(eventDTO, ev);
                    return eventRepository.save(ev);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventDTO.getId())));
        return eventMapper.toDTO(event);
    }

    @Override
    @Transactional
    public boolean deleteById(long eventId) {
        boolean isDeletedEvent = eventRepository.findById(eventId)
                .map(event -> {
                    eventRepository.deleteById(event.getId());
                    return true;
                }).orElseThrow(EntityNotFoundException::new);
        log.info("Method *** deleteById *** : isDeletedEvent = {} with ID = {}", isDeletedEvent, eventId);
        return isDeletedEvent;
    }

    @Override
    @Transactional
    public void deleteAll() {
        eventRepository.deleteAll();
    }

}
