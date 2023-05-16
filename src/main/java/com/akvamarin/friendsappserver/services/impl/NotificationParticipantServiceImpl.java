package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.request.NotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationFeedbackDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
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
import com.akvamarin.friendsappserver.services.NotificationParticipantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationParticipantServiceImpl implements NotificationParticipantService {

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final NotificationParticipantRepository notificationParticipantRepository;

    private final NotificationMapper notificationMapper;

    private final EventMapper eventMapper;

    private final UserMapper userMapper;

    /**
     * пользователь подал заявку на участие
     * в эвенте (нажал на кнопку)
     */
    @Transactional
    public NotificationParticipant createParticipantRequest(NotificationDTO notificationDTO) {
        Long eventId = notificationDTO.getEventId();
        Long userId = notificationDTO.getUserId();

        // Дублирующие заявки не сохранять
        boolean exists = notificationParticipantRepository.existsByEventIdAndUserId(eventId, userId);
        if (exists) {
            throw new ValidationException("Notification already exists!");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with ID: " + eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Если участник и есть орг мероприятия:
        if (event.getUser().getId().equals(userId)) {
            throw new ValidationException("The organizer cannot apply for their own event!");
        }

        NotificationParticipant notificationParticipant = NotificationParticipant.builder()
                .event(event)
                .user(user)
                .feedbackType(FeedbackType.WAITING)
                .build();

        NotificationParticipant savedNotificationParticipant = notificationParticipantRepository.save(notificationParticipant);
        log.info("Method *** createParticipantRequest *** : Participant request created successfully." +
                "eventId = {} UserId = {}", eventId, userId);
        return savedNotificationParticipant;
    }

    /**
     * орг обновляет статус заявки
     * одобряет или отклоняет
     */
    @Transactional
    public ViewNotificationDTO updateFeedbackStatus(NotificationFeedbackDTO notificationFeedbackDTO) {
        Long requestId = notificationFeedbackDTO.getId();
        FeedbackType feedbackType = notificationFeedbackDTO.getFeedbackType();

        NotificationParticipant notification = notificationParticipantRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + requestId));

        notification.setFeedbackType(feedbackType);
        notification.setUpdatedAt(LocalDateTime.now());
        notification.setOwnerViewed(true);

        NotificationParticipant updatedNotification = notificationParticipantRepository.save(notification);
        log.info("Method *** updateFeedbackStatus *** : Request ID = {} FeedbackType = {} updated successfully.",
                requestId, feedbackType);
        return notificationMapper.toDTO(updatedNotification);
    }

    /**
     * отмена заявки на эвент пользователем
     * (пользователь (не орг) повторно нажал на кнопку)
     */
    @Transactional
    public void deleteParticipantRequest(Long requestId) {
        notificationParticipantRepository.deleteById(requestId);
        log.info("Method *** deleteParticipantRequest *** : Participant request ID = {} deleted successfully.", requestId);
    }


    /**
     * неподтвержденные заявки как для орга и
     * и результат заявок как для участника
     * для конкретного пользователя (не были закрыты на кнопку)
     */
    @Transactional(readOnly = true)
    public List<ViewNotificationDTO> findNotificationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<NotificationParticipant> ownerNotifications = notificationParticipantRepository.findByEventUserAndOwnerViewedFalse(user);
        List<NotificationParticipant> participantNotifications = notificationParticipantRepository
                .findByUserAndParticipantViewedFalseAndFeedbackTypeIn(user, Arrays.asList(FeedbackType.APPROVED, FeedbackType.REJECTED));

        List<ViewNotificationDTO> allNotifications = new ArrayList<>();

        for (NotificationParticipant notification : ownerNotifications) {
            ViewNotificationDTO dto = notificationMapper.toDTO(notification);
            dto.setForOwner(true);
            allNotifications.add(dto);
        }

        for (NotificationParticipant notification : participantNotifications) {
            Event event = notification.getEvent();
            User organizer = event.getUser();
            ViewUserSlimDTO organizerDTO = userMapper.userToViewUserSlimDTO(organizer);

            ViewNotificationDTO dto = notificationMapper.toDTO(notification);
            dto.setForOwner(false);
            dto.setUserId(organizerDTO.getId());
            dto.setUserNickname(organizerDTO.getNickname());
            dto.setUserUrlAvatar(organizerDTO.getUrlAvatar());
            allNotifications.add(dto);
        }

        allNotifications.sort(Comparator.comparing(ViewNotificationDTO::getUpdatedAt).reversed());
        log.info("Method *** findNotificationsByUserId *** : user ID = {} notification size = {}", userId, allNotifications.size());
        return allNotifications;
    }

    /**
     * выбирает все эвенты, где пользователь
     * является участником, т.е. точно идет на мероприятие
     */
    @Transactional(readOnly = true)
    public List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(Long userId) {
        List<NotificationParticipant> participantEvents = notificationParticipantRepository
                .findByUserIdAndFeedbackType(userId, FeedbackType.APPROVED);
        List<Event> organizerEvents = eventRepository.findByUserId(userId);

        List<ViewEventDTO> userEvents = new ArrayList<>();

        for (NotificationParticipant participant : participantEvents) {
            Event event = participant.getEvent();
            userEvents.add(eventMapper.toDTO(event));
        }

        for (Event event : organizerEvents) {
            userEvents.add(eventMapper.toDTO(event));
        }

        userEvents.sort(Comparator.comparing(ViewEventDTO::getDate));

        log.info("Method *** findUserEventsWithApprovedFeedbackAndOrganizer *** : user ID = {} List<ViewEventDTO> size = {}",
                userId, userEvents.size());
        return userEvents;
    }

    /**
     * выбирает все эвенты, где пользователь
     * ждет одобрения от организатора мероприятия
     */
    @Transactional(readOnly = true)
    public List<ViewEventDTO> findUserEventsWithWaitingFeedback(Long userId) {
        List<NotificationParticipant> participantEvents = notificationParticipantRepository
                .findByUserIdAndFeedbackType(userId, FeedbackType.WAITING);

        List<ViewEventDTO> userEvents = new ArrayList<>();

        for (NotificationParticipant participant : participantEvents) {
            Event event = participant.getEvent();
            userEvents.add(eventMapper.toDTO(event));
        }

        log.info("Method *** findUserEventsWithWaitingFeedback *** : user ID = {} List<ViewEventDTO> size = {}",
                userId, userEvents.size());
        return userEvents;
    }

    /**
     * выбирает всех участников одного эвента
     * в том числе и организатора
     */
    @Transactional(readOnly = true)
    public List<ViewUserSlimDTO> findEventParticipantsWithApprovedFeedback(Long eventId) {
        List<NotificationParticipant> participants = notificationParticipantRepository.findByEventIdAndFeedbackType(eventId, FeedbackType.APPROVED);

        List<ViewUserSlimDTO> eventParticipants = new ArrayList<>();

        for (NotificationParticipant participant : participants) {
            ViewUserSlimDTO userDTO = userMapper.userToViewUserSlimDTO(participant.getUser());
            eventParticipants.add(userDTO);
        }

        if (!participants.isEmpty()) {
            Event event = participants.get(0).getEvent();
            User organizer = event.getUser();
            ViewUserSlimDTO organizerDTO = userMapper.userToViewUserSlimDTO(organizer);
            eventParticipants.add(organizerDTO);
        }

        log.info("Method *** findEventParticipantsWithApprovedFeedback *** : event ID = {} eventParticipants size = {}",
                eventId, eventParticipants.size());
        return eventParticipants;
    }

    /** ( Не понадобился, см. метод updateFeedbackStatus() )
     * отвечает за вывод заявок оргу
     * если орг нажал кнопку "закрыть" == true
     */
    @Transactional
    public void updateOwnerViewed(Long requestId, boolean ownerViewed) {
        NotificationParticipant notificationParticipant = notificationParticipantRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + requestId));

        notificationParticipant.setOwnerViewed(ownerViewed);
        notificationParticipantRepository.save(notificationParticipant);
        log.info("Method *** updateOwnerViewed *** : notification with ID = {}, ownerViewed = {}", requestId, ownerViewed);
    }

    /**
     * отвечает за вывод заявок-ответов пользователю
     * если пользователь нажал кнопку "закрыть" == true
     */
    @Transactional
    public void updateParticipantViewed(Long requestId, boolean participantViewed) {
        NotificationParticipant notificationParticipant = notificationParticipantRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + requestId));

        notificationParticipant.setParticipantViewed(participantViewed);
        notificationParticipantRepository.save(notificationParticipant);
        log.info("Method *** updateParticipantViewed *** : notification with ID = {}, participantViewed = {}", requestId, participantViewed);
    }

    @Transactional(readOnly = true)
    public boolean isExistNotification(NotificationDTO notificationDTO) {
        Long eventId = notificationDTO.getEventId();
        Long userId = notificationDTO.getUserId();

        log.info("Method *** isExistNotification *** : userId = {} eventId = {}", userId, eventId);
        return notificationParticipantRepository.existsByEventIdAndUserId(eventId, userId);
    }

    /**
     * ищет заявку участника по
     * конкретному мероприятию
     */
    @Transactional(readOnly = true)
    public ViewNotificationDTO findNotificationByUserIdEventId(Long eventId, Long userId) {

        NotificationParticipant notificationParticipant = notificationParticipantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found for event ID: " + eventId + " and user ID: " + userId));

        log.info("Method *** findNotificationByUserIdEventId *** : userId = {} eventId = {}", userId, eventId);

        return notificationMapper.toDTO(notificationParticipant);
    }


}
