package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.mapper.event.EventMapper;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.services.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    private static final long EVENT_ID = 1L;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createNewEvent_validInput_returnCreatedEvent() {
        EventDTO eventDTO = new EventDTO();
        Event event = new Event();
        when(eventMapper.toEntity(eventDTO)).thenReturn(event);
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toDTO(event)).thenReturn(eventDTO);

        // Actual
        EventDTO createdEventDTO = eventService.createNewEvent(eventDTO);

        // Expected & Actual
        assertEquals(eventDTO, createdEventDTO);
    }

    /**
     * при вводе недопустимых данных создается исключение ConstraintViolationException.
     * */
    @Test
    void createNewEvent_invalidInput_throwConstraintViolationException() {
        EventDTO eventDTO = new EventDTO();

        // Expected & Actual
        assertThrows(ConstraintViolationException.class, () -> eventService.createNewEvent(eventDTO));
    }

    /**
     * проверка, на кол-во возвращаемых элементов списке
     * */
    @Test
    public void testFindAll() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDTO(events.get(0))).thenReturn(new EventDTO());
        when(eventMapper.toDTO(events.get(1))).thenReturn(new EventDTO());

        // Actual
        List<EventDTO> eventDTOs = eventService.findAll();

        // Expected 2
        assertEquals(2, eventDTOs.size());
    }

    /**
     * когда мероприятие найдено по ID
     * */
    @Test
    public void findById_validInput_returnFoundEvent() {
        Event event = new Event();
        EventDTO expectedEventDTO = new EventDTO();
        Mockito.when(eventRepository.findById(EVENT_ID )).thenReturn(Optional.of(event));
        Mockito.when(eventMapper.toDTO(event)).thenReturn(expectedEventDTO);

        // Actual
        EventDTO foundEventDTO = eventService.findById(EVENT_ID );

        assertEquals(expectedEventDTO, foundEventDTO);
        Mockito.verify(eventRepository).findById(EVENT_ID);
    }

    /**
     * когда мероприятие не найдено, возникает исключение EntityNotFoundException
     * */
    @Test
    public void findById_invalidInput_throwEntityNotFoundException() {
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        // Expected & Actual
        assertThrows(EntityNotFoundException.class, () -> eventService.findById(EVENT_ID));
        Mockito.verify(eventRepository).findById(EVENT_ID);
    }

    /**
     *  возвращает обновленное мероприятие с тем же ID
     * */
    @Test
    void updateEvent_validInput_returnUpdatedEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(1L);
        eventDTO.setName("Updated name for Event");
        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Original name for Event");
        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(existingEvent);

        // Actual
        EventDTO updatedEventDTO = eventService.updateEvent(eventDTO);

        assertEquals(eventDTO.getId(), updatedEventDTO.getId());
        assertEquals(eventDTO.getName(), updatedEventDTO.getName());
        Mockito.verify(eventMapper).updateEntity(eventDTO, existingEvent);
        Mockito.verify(eventRepository).save(existingEvent);
    }

    /**
     *  когда нет мероприятия в репозитории, возвращаем исключение
     * */
    @Test
    void updateEvent_invalidInput_throwEntityNotFoundException() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(EVENT_ID);
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        // Expected & Actual
        assertThrows(EntityNotFoundException.class, () -> eventService.updateEvent(eventDTO));
    }

    /**
     *  когда мероприятие успешно удалено
     * */
    @Test
    void deleteById_existingEvent_returnTrue() {
        Event event = new Event();
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        // Actual
        boolean isDeleted = eventService.deleteById(EVENT_ID);

        // Assert
        assertTrue(isDeleted);
        Mockito.verify(eventRepository).deleteById(EVENT_ID);
    }

    /**
     *  когда нет мероприятия для удаления
     * */
    @Test
    void deleteById_nonExistingEvent_throwEntityNotFoundException() {
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        // Expected & Actual
        assertThrows(EntityNotFoundException.class, () -> eventService.deleteById(EVENT_ID));
    }

}