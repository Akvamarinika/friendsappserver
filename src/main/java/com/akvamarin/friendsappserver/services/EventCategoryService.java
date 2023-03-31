package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface EventCategoryService {
    EventCategoryDTO createNew(EventCategoryDTO eventCategoryDTO);

    List<EventCategoryDTO> saveAll(List<EventCategoryDTO> eventCategoryDTOs);

    List<EventCategoryDTO> findAll();

    EventCategoryDTO findById(Long categoryId);

    EventCategoryDTO update(EventCategoryDTO eventCategoryDTO);

    boolean deleteById(Long categoryId);

    void deleteAll();

}
