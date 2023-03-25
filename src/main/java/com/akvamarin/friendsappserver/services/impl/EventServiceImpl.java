package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.EventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.mapper.EventMapper;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.services.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    @Override
    public EventDTO createNewEvent(@NotNull EventDTO eventDTO) {
        Event event = eventMapper.toEntity(eventDTO);
        return eventMapper.toDTO(eventRepository.save(event));
    }

    @Transactional
    @Override
    public List<EventDTO> findAll() {
        List<EventDTO> result = eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : EventDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public EventDTO findById(long eventId) {
        EventDTO result = eventRepository.findById(eventId)
                .map(eventMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with ID %d not found", eventId)));
        log.info("Method *** findById *** : EventDTO = {} EventID = {}", result, eventId);
        return result;
    }

    @Override
    @Transactional
    public EventDTO updateEvent(@NonNull EventDTO eventDTO) {
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

}
