package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.request.NotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationFeedbackDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.enums.FeedbackType;
import com.akvamarin.friendsappserver.domain.mapper.UserMapper;
import com.akvamarin.friendsappserver.domain.mapper.event.EventMapper;
import com.akvamarin.friendsappserver.domain.mapper.event.NotificationMapper;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.repositories.NotificationParticipantRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.impl.NotificationParticipantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class NotificationParticipantServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private NotificationParticipantRepository notificationParticipantRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private NotificationParticipantServiceImpl notificationParticipantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Успешное создание заявки на мероприятие
     * **/
    @Test
    void createParticipantRequest_UniqueRequest_SuccessfullyCreated() {
        // Arrange
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEventId(1L);
        notificationDTO.setUserId(2L);

        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        event.setUser(user); // Set the user as the organizer for the event

        when(notificationParticipantRepository.existsByEventIdAndUserId(1L, 2L)).thenReturn(false);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        NotificationParticipant savedNotificationParticipant = new NotificationParticipant();
        when(notificationParticipantRepository.save(any(NotificationParticipant.class))).thenReturn(savedNotificationParticipant);

        // actual
        NotificationParticipant result = notificationParticipantService.createParticipantRequest(notificationDTO);

        assertNotNull(result);
        verify(notificationParticipantRepository, times(1)).existsByEventIdAndUserId(1L, 2L);
        verify(eventRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(2L);
        verify(notificationParticipantRepository, times(1)).save(any(NotificationParticipant.class));
    }



    @Test
    void createParticipantRequest_DuplicateRequest_ThrowsValidationException() {
        // Arrange
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEventId(1L);
        notificationDTO.setUserId(2L);

        when(notificationParticipantRepository.existsByEventIdAndUserId(1L, 2L)).thenReturn(true);

        // Act & Assert
        assertThrows(ValidationException.class, () -> notificationParticipantService.createParticipantRequest(notificationDTO));
        verify(notificationParticipantRepository, times(1)).existsByEventIdAndUserId(1L, 2L);
        verify(eventRepository, never()).findById(anyLong());
        verify(userRepository, never()).findById(anyLong());
        verify(notificationParticipantRepository, never()).save(any(NotificationParticipant.class));
    }

    /**
     * Когда организатор мероприятия подает
     * заявку на свое же мероприятие
     * **/
    @Test
    void createParticipantRequest_OrganizerApplyingForOwnEvent_ThrowsValidationException() {
        // Arrange
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEventId(1L);
        notificationDTO.setUserId(1L);

        Event event = new Event();
        event.setId(1L);
        User user = new User();
        user.setId(1L);
        event.setUser(user); //user as organizer for event

        when(notificationParticipantRepository.existsByEventIdAndUserId(1L, 1L)).thenReturn(false);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () -> notificationParticipantService.createParticipantRequest(notificationDTO));
        verify(notificationParticipantRepository, times(1)).existsByEventIdAndUserId(1L, 1L);
        verify(eventRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(notificationParticipantRepository, never()).save(any(NotificationParticipant.class));
    }

    /**
     * Успешное обновление статуса заявки на мероприятие
     * **/
    @Test
    void updateFeedbackStatus_ValidRequest_SuccessfullyUpdated() {
        NotificationFeedbackDTO notificationFeedbackDTO = new NotificationFeedbackDTO();
        notificationFeedbackDTO.setId(1L);
        notificationFeedbackDTO.setFeedbackType(FeedbackType.APPROVED);

        NotificationParticipant notificationParticipant = new NotificationParticipant();
        notificationParticipant.setId(1L);

        when(notificationParticipantRepository.findById(1L)).thenReturn(Optional.of(notificationParticipant));
        when(notificationParticipantRepository.save(any(NotificationParticipant.class))).thenReturn(notificationParticipant);
        when(notificationMapper.toDTO(notificationParticipant)).thenReturn(new ViewNotificationDTO());

        // Actual
        ViewNotificationDTO result = notificationParticipantService.updateFeedbackStatus(notificationFeedbackDTO);

        assertNotNull(result);
        assertEquals(FeedbackType.APPROVED, notificationParticipant.getFeedbackType());
        assertTrue(notificationParticipant.isOwnerViewed());
        verify(notificationParticipantRepository, times(1)).findById(1L);
        verify(notificationParticipantRepository, times(1)).save(notificationParticipant);
        verify(notificationMapper, times(1)).toDTO(notificationParticipant);
    }


    /**
     * Обновление статуса заявки на мероприятие
     * когда нет существ-ей заявки
     * **/
    @Test
    void updateFeedbackStatus_InvalidRequest_ThrowsEntityNotFoundException() {
        NotificationFeedbackDTO notificationFeedbackDTO = new NotificationFeedbackDTO();
        notificationFeedbackDTO.setId(1L);
        notificationFeedbackDTO.setFeedbackType(FeedbackType.APPROVED);

        when(notificationParticipantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> notificationParticipantService.updateFeedbackStatus(notificationFeedbackDTO));
        verify(notificationParticipantRepository, times(1)).findById(1L);
        verify(notificationParticipantRepository, never()).save(any(NotificationParticipant.class));
    }

    /**
     * Успешное удаление заявки на мероприятие
     * **/
    @Test
    void deleteParticipantRequest_ValidRequest_SuccessfullyDeleted() {
        Long requestId = 1L;

        // actual
        notificationParticipantService.deleteParticipantRequest(requestId);

        verify(notificationParticipantRepository, times(1)).deleteById(requestId);
    }

    /**
     * Проверка на существование заявки,
     * заявка существует
     * **/
    @Test
    void isExistNotification_NotificationExists_ReturnsTrue() {
        Long eventId = 1L;
        Long userId = 1L;
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEventId(eventId);
        notificationDTO.setUserId(userId);

        when(notificationParticipantRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(true);

        //actual
        boolean result = notificationParticipantService.isExistNotification(notificationDTO);

        assertTrue(result);
    }

    /**
     * Проверка на существование заявки,
     * заявка не найдена
     * **/
    @Test
    void isExistNotification_NotificationDoesNotExist_ReturnsFalse() {
        // Arrange
        Long eventId = 1L;
        Long userId = 1L;
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setEventId(eventId);
        notificationDTO.setUserId(userId);

        when(notificationParticipantRepository.existsByEventIdAndUserId(eventId, userId)).thenReturn(false);

        //actual
        boolean result = notificationParticipantService.isExistNotification(notificationDTO);

        assertFalse(result);
    }

    /**
     * ищет заявку участника по
     * конкретному мероприятию - когда есть заявка
     */
    @Test
    void findNotificationByUserIdEventId_NotificationExists_ReturnsViewNotificationDTO() {
        Long eventId = 1L;
        Long userId = 1L;
        NotificationParticipant notificationParticipant = new NotificationParticipant();
        notificationParticipant.setId(1L);

        when(notificationParticipantRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.of(notificationParticipant));

        //actual
        ViewNotificationDTO result = notificationParticipantService.findNotificationByUserIdEventId(eventId, userId);

        assertNotNull(result);
        verify(notificationParticipantRepository, times(1))
                .findByEventIdAndUserId(eventId, userId);
    }

    /**
     * ищет заявку участника по
     * конкретному мероприятию - когда нет заявки
     */
    @Test
    void findNotificationByUserIdEventId_NotificationDoesNotExist_ThrowsEntityNotFoundException() {
        Long eventId = 1L;
        Long userId = 1L;

        when(notificationParticipantRepository.findByEventIdAndUserId(eventId, userId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> notificationParticipantService.findNotificationByUserIdEventId(eventId, userId));
        verify(notificationParticipantRepository, times(1))
                .findByEventIdAndUserId(eventId, userId);
    }


}
