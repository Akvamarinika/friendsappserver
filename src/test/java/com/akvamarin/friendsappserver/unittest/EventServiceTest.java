package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.akvamarin.friendsappserver.domain.mapper.event.EventCategoryMapper;
import com.akvamarin.friendsappserver.domain.mapper.event.EventMapper;
import com.akvamarin.friendsappserver.repositories.EventCategoryRepository;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    private static final long EVENT_ID = 1L;
    private static final long CATEGORY_ID = 1L;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventCategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventCategoryMapper eventCategoryMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setup() {
        EventCategory eventCategory = new EventCategory();
        eventCategory.setId(1L);
        eventCategory.setName("Category #1");

        categoryRepository.save(eventCategory);
    }


    @Test
    void createNewEvent_validInput_returnCreatedEvent() {
        EventCategory category = new EventCategory();
        category.setId(1L);
        category.setName("Test category");
        categoryRepository.save(category);

        User user = new User();
        user.setId(1L);
        user.setUsername("test@example.com");
        user.setEmail("test@example.com");
        user.setNickname("Test");
        userRepository.save(user);

        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Test event");
        eventDTO.setDescription("Test description");
        eventDTO.setDate(LocalDate.now());
        eventDTO.setPeriodOfTime(PeriodOfTime.EVENING);
        eventDTO.setPartner(Partner.ANY);
        eventDTO.setEventCategoryId(category.getId());
        eventDTO.setOwnerId(user.getId());

        Event expectedEvent = new Event();
        expectedEvent.setId(EVENT_ID);
        expectedEvent.setName(eventDTO.getName());
        expectedEvent.setDescription(eventDTO.getDescription());
        expectedEvent.setDate(eventDTO.getDate());
        expectedEvent.setPeriodOfTime(eventDTO.getPeriodOfTime());
        expectedEvent.setPartner(eventDTO.getPartner());
        expectedEvent.setEventCategory(category);
        expectedEvent.setUser(user);

        when(eventMapper.toEntity(Mockito.any(EventDTO.class))).thenReturn(expectedEvent);
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(eventRepository.save(expectedEvent)).thenReturn(expectedEvent);

        // Actual
        Event createdEvent = eventService.createNewEvent(eventDTO);

        // Expected & Actual
        assertNotNull(createdEvent.getId());
        assertEquals(eventDTO.getName(), createdEvent.getName());
        assertEquals(eventDTO.getDescription(), createdEvent.getDescription());
        assertEquals(eventDTO.getDate(), createdEvent.getDate());
        assertEquals(eventDTO.getPeriodOfTime(), createdEvent.getPeriodOfTime());
        assertEquals(eventDTO.getPartner(), createdEvent.getPartner());
        assertEquals(category.getName(), createdEvent.getEventCategory().getName());
        assertEquals(user.getId(), createdEvent.getUser().getId());

        verify(eventMapper).toEntity(eventDTO);
        verify(categoryRepository).findById(category.getId());
        verify(userRepository).findById(user.getId());
    }

    /**
     * при вводе недопустимых данных создается исключение ConstraintViolationException.
     * */
    @Test
    void createNewEvent_invalidInput_throwEntityNotFoundException() {
        EventDTO eventDTO = new EventDTO();

        // Expected & Actual
        assertThrows(EntityNotFoundException.class, () -> eventService.createNewEvent(eventDTO));
    }

    /**
     * проверка, на кол-во возвращаемых элементов списке
     * */
    @Test
    public void testFindAll() {
        List<Event> events = Arrays.asList(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toDTO(events.get(0))).thenReturn(new ViewEventDTO());
        when(eventMapper.toDTO(events.get(1))).thenReturn(new ViewEventDTO());

        // Actual
        List<ViewEventDTO> eventDTOs = eventService.findAll();

        // Expected 2
        assertEquals(2, eventDTOs.size());
    }

    /**
     * когда мероприятие найдено по ID
     * */
    @Test
    public void findById_validInput_returnFoundEvent() {
        Event event = new Event();
        ViewEventDTO expectedEventDTO = new ViewEventDTO();
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
        Mockito.when(eventMapper.toDTO(event)).thenReturn(expectedEventDTO);

        // Actual
        ViewEventDTO foundEventDTO = eventService.findById(EVENT_ID);

        assertEquals(expectedEventDTO, foundEventDTO);
        verify(eventRepository).findById(EVENT_ID);
    }

    /**
     * когда мероприятие не найдено, возникает исключение EntityNotFoundException
     * */
    @Test
    public void findById_invalidInput_throwEntityNotFoundException() {
        Mockito.when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());

        // Expected & Actual
        assertThrows(EntityNotFoundException.class, () -> eventService.findById(EVENT_ID));
        verify(eventRepository).findById(EVENT_ID);
    }

    /**
     *  возвращает обновленное мероприятие с тем же ID
     * */
    @Test
    void updateEvent_validInput_returnUpdatedEvent() {
        Event existingEvent = new Event();
        existingEvent.setId(EVENT_ID);
        existingEvent.setName("Original name for Event");

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(EVENT_ID);
        eventDTO.setName("Updated name for Event");
        eventDTO.setEventCategoryId(CATEGORY_ID);

        ViewEventDTO expectedEventDTO = new ViewEventDTO();
        expectedEventDTO.setId(EVENT_ID);
        expectedEventDTO.setName("Updated name for Event");

        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(existingEvent)).thenReturn(existingEvent);
        when(eventMapper.toDTO(existingEvent)).thenReturn(expectedEventDTO);

        EventCategory eventCategory = new EventCategory();
        eventCategory.setId(CATEGORY_ID);
        eventCategory.setName("Event Category Name");

        EventCategoryDTO eventCategoryDTO = new EventCategoryDTO();
        eventCategoryDTO.setId(CATEGORY_ID);
        eventCategoryDTO.setName("Event Category Name");

        when(eventCategoryMapper.toDTO(eventCategory)).thenReturn(eventCategoryDTO);

        // Actual
        ViewEventDTO updatedEventDTO = eventService.updateEvent(eventDTO);

        // Expected expectedEventDTO
        assertNotNull(updatedEventDTO);
        assertEquals(expectedEventDTO.getId(), updatedEventDTO.getId());
        assertEquals(expectedEventDTO.getName(), updatedEventDTO.getName());
        assertEquals(expectedEventDTO.getEventCategory(), eventCategoryDTO);

        verify(eventRepository).findById(EVENT_ID);
        verify(eventRepository).save(existingEvent);
        verify(eventMapper).toDTO(existingEvent);
        verify(eventCategoryMapper).toDTO(eventCategory);
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
        event.setId(EVENT_ID);
        // when(eventRepository.findById(anyLong())).thenReturn(java.util.Optional.of(event));
        // doNothing().when(eventRepository).deleteById(anyLong());
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));

        // Actual
        boolean isDeleted = eventService.deleteById(EVENT_ID);

        // Assert
        assertTrue(isDeleted);
        verify(eventRepository).deleteById(EVENT_ID);
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