package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.mapper.event.EventCategoryMapper;
import com.akvamarin.friendsappserver.repositories.EventCategoryRepository;
import com.akvamarin.friendsappserver.services.EventCategoryService;
import com.akvamarin.friendsappserver.services.impl.EventCategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class EventCategoryServiceTest {
    private EventCategoryRepository eventCategoryRepository;
    private EventCategoryMapper eventCategoryMapper;
    private EventCategoryService eventCategoryService;

    @BeforeEach
    void setUp() {
        eventCategoryRepository = mock(EventCategoryRepository.class);
        eventCategoryMapper = mock(EventCategoryMapper.class);
        eventCategoryService = new EventCategoryServiceImpl(eventCategoryRepository, eventCategoryMapper);
    }

    @Test
    void createNew_ValidEventCategoryDTO_ReturnsEventCategoryDTO() {
        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();
        EventCategory eventCategory = new EventCategory();
        EventCategory savedEventCategory = new EventCategory();
        when(eventCategoryMapper.toEntity(eventCategoryDTO)).thenReturn(eventCategory);
        when(eventCategoryRepository.save(eventCategory)).thenReturn(savedEventCategory);
        when(eventCategoryMapper.toDTO(savedEventCategory)).thenReturn(eventCategoryDTO);

        // actual
        EventCategoryDTO result = eventCategoryService.createNew(eventCategoryDTO);

        assertNotNull(result);
        assertEquals(eventCategoryDTO, result);
        verify(eventCategoryMapper, times(1)).toEntity(eventCategoryDTO);
        verify(eventCategoryRepository, times(1)).save(eventCategory);
        verify(eventCategoryMapper, times(1)).toDTO(savedEventCategory);
    }

    @Test
    void findAll_ReturnsEventCategoryDTOList() {
        List<EventCategory> eventCategories = new ArrayList<>();
        List<EventCategoryDTO> eventCategoryDTOs = new ArrayList<>();

        EventCategory eventCategory = new EventCategory();
        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();

        eventCategories.add(eventCategory);
        eventCategoryDTOs.add(eventCategoryDTO);

        when(eventCategoryRepository.findAll()).thenReturn(eventCategories);
        when(eventCategoryMapper.toDTO(eventCategory)).thenReturn(eventCategoryDTO);

        // actual
        List<EventCategoryDTO> result = eventCategoryService.findAll();

        assertNotNull(result);
        assertEquals(eventCategoryDTOs, result);
        verify(eventCategoryRepository, times(1)).findAll();
        verify(eventCategoryMapper, times(1)).toDTO(eventCategory);
    }


    @Test
    void findById_ExistingEventCategoryId_ReturnsEventCategoryDTO() {
        Long eventCategoryId = 1L;
        EventCategory eventCategory = new EventCategory();
        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();
        when(eventCategoryRepository.findById(eventCategoryId)).thenReturn(Optional.of(eventCategory));
        when(eventCategoryMapper.toDTO(eventCategory)).thenReturn(eventCategoryDTO);

        // actual
        EventCategoryDTO result = eventCategoryService.findById(eventCategoryId);

        assertNotNull(result);
        assertEquals(eventCategoryDTO, result);
        verify(eventCategoryRepository, times(1)).findById(eventCategoryId);
        verify(eventCategoryMapper, times(1)).toDTO(eventCategory);
    }

    @Test
    void findById_NonExistingEventCategoryId_ThrowsEntityNotFoundException() {
        Long eventCategoryId = 1L;
        when(eventCategoryRepository.findById(eventCategoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventCategoryService.findById(eventCategoryId));
        verify(eventCategoryRepository, times(1)).findById(eventCategoryId);
    }

    @Test
    void update_ExistingEventCategoryDTO_ReturnsUpdatedEventCategoryDTO() {
        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();
        eventCategoryDTO.setId(1L);

        EventCategory eventCategory = new EventCategory();
        EventCategory updatedEventCategory = new EventCategory();

        when(eventCategoryRepository.findById(eventCategoryDTO.getId())).thenReturn(Optional.of(eventCategory));
        when(eventCategoryRepository.save(eventCategory)).thenReturn(updatedEventCategory);
        when(eventCategoryMapper.toDTO(updatedEventCategory)).thenReturn(eventCategoryDTO);

        // actual
        EventCategoryDTO result = eventCategoryService.update(eventCategoryDTO);

        assertNotNull(result);
        assertEquals(eventCategoryDTO, result);
        verify(eventCategoryRepository, times(1)).findById(eventCategoryDTO.getId());
        verify(eventCategoryMapper, times(1)).updateEntity(eventCategoryDTO, eventCategory);
        verify(eventCategoryRepository, times(1)).save(eventCategory);
        verify(eventCategoryMapper, times(1)).toDTO(updatedEventCategory);
    }


    @Test
    void update_NonExistingEventCategoryDTO_ThrowsEntityNotFoundException() {
        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();
        eventCategoryDTO.setId(1L);

        when(eventCategoryRepository.findById(eventCategoryDTO.getId())).thenReturn(Optional.empty());

        // actual
        assertThrows(EntityNotFoundException.class, () -> eventCategoryService.update(eventCategoryDTO));
        verify(eventCategoryRepository, times(1)).findById(eventCategoryDTO.getId());
    }


    @Test
    void deleteById_ExistingEventCategoryId_ReturnsTrue() {
        Long eventCategoryId = 1L;
        EventCategory eventCategory = new EventCategory();
        when(eventCategoryRepository.findById(eventCategoryId)).thenReturn(Optional.of(eventCategory));

        // actual
        boolean result = eventCategoryService.deleteById(eventCategoryId);

        assertTrue(result);
        verify(eventCategoryRepository, times(1)).findById(eventCategoryId);
        verify(eventCategoryRepository, times(1)).deleteById(eventCategory.getId());
    }

    @Test
    void deleteById_NonExistingEventCategoryId_ThrowsEntityNotFoundException() {
        Long eventCategoryId = 1L;
        when(eventCategoryRepository.findById(eventCategoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> eventCategoryService.deleteById(eventCategoryId));
        verify(eventCategoryRepository, times(1)).findById(eventCategoryId);
    }

    @Test
    void deleteAll() {
        // actual
        eventCategoryService.deleteAll();

        verify(eventCategoryRepository, times(1)).deleteAll();
    }
}
