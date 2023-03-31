package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.mapper.EventCategoryMapper;
import com.akvamarin.friendsappserver.repositories.EventCategoryRepository;
import com.akvamarin.friendsappserver.services.EventCategoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCategoryServiceImpl implements EventCategoryService {
    private final EventCategoryRepository eventCategoryRepository;
    private final EventCategoryMapper eventCategoryMapper;

    @Transactional
    @Override
    public EventCategoryDTO createNew(@NotNull EventCategoryDTO eventCategoryDTO) {
        EventCategory eventCategory = eventCategoryMapper.toEntity(eventCategoryDTO);
        return eventCategoryMapper.toDTO(eventCategoryRepository.save(eventCategory));
    }

    @Transactional
    @Override
    public List<EventCategoryDTO> saveAll(List<EventCategoryDTO> eventCategoryDTOs) {
        List<EventCategory> eventCategories = eventCategoryDTOs.stream()
                .map(eventCategoryMapper::toEntity)
                .collect(Collectors.toList());
        List<EventCategory> savedEventCategories = eventCategoryRepository.saveAll(eventCategories);
        return savedEventCategories.stream()
                .map(eventCategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * используется при создании / обновлении /
     * выводе всех мероприятий
     * */
    @Transactional
    @Override
    public List<EventCategoryDTO> findAll() {
        List<EventCategoryDTO> result = eventCategoryRepository.findAll().stream()
                .map(eventCategoryMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : EventCategoryDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public EventCategoryDTO findById(Long eventCategoryId) {
        EventCategoryDTO result = eventCategoryRepository.findById(eventCategoryId)
                .map(eventCategoryMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event category with ID %d not found", eventCategoryId)));
        log.info("Method *** findById *** : EventCategoryDTO = {} EventCategoryId = {}", result, eventCategoryId);
        return result;
    }

    @Override
    @Transactional
    public EventCategoryDTO update(@NonNull EventCategoryDTO eventCategoryDTO) {
        EventCategory eventCategory = eventCategoryRepository.findById(eventCategoryDTO.getId())
                .map(ec -> {
                    eventCategoryMapper.updateEntity(eventCategoryDTO, ec);
                    return eventCategoryRepository.save(ec);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event category with ID %d not found", eventCategoryDTO.getId())));
        return eventCategoryMapper.toDTO(eventCategory);
    }

    @Override
    @Transactional
    public boolean deleteById(Long eventCategoryId) {
        boolean isDeletedEventCategory = eventCategoryRepository.findById(eventCategoryId)
                .map(eventCategory -> {
                    eventCategoryRepository.deleteById(eventCategory.getId());
                    return true;
                }).orElseThrow(EntityNotFoundException::new);
        log.info("Method *** deleteById *** : isDeletedEventCategory = {} with ID = {}", isDeletedEventCategory, eventCategoryId);
        return isDeletedEventCategory;
    }

    @Override
    @Transactional
    public void deleteAll() {
        eventCategoryRepository.deleteAll();
    }
}
