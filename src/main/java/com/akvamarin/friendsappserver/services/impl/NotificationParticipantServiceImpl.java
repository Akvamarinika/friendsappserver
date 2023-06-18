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
import com.akvamarin.friendsappserver.domain.enums.ParticipantFilterType;
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
import java.util.stream.Collectors;

/**
 * Сервис по работе с заявками,
 * уведомлениями на мероприятия и участниками.
 *
 * @see NotificationParticipant
 * @see UserRepository
 * @see EventRepository
 * @see NotificationParticipantRepository
 * @see NotificationMapper
 * @see EventMapper
 * @see UserMapper
 * */

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
     * Создает новую заявку от пользователя на участие в мероприятии.
     *
     * @param notificationDTO информация о заявке
     * @return NotificationParticipant - созданная заявка на участие
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
     * Обновляет статус заявки (одобряет или отклоняет) организатор.
     *
     * @param notificationFeedbackDTO информация об обновлении статуса заявки
     * @return обновленная информация о заявке
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
     * Пользователь отменяет заявку на участие в мероприятии.
     *
     * @param requestId идентификатор заявки
     */
    @Transactional
    public void deleteParticipantRequest(Long requestId) {
        notificationParticipantRepository.deleteById(requestId);
        log.info("Method *** deleteParticipantRequest *** : Participant request ID = {} deleted successfully.", requestId);
    }


    /**
     * Неподтвержденные заявки как для организатора, так и результат заявок для участника
     * для конкретного пользователя (не были закрыты по кнопке)
     *
     * @param userId идентификатор пользователя
     * @return список объектов ViewNotificationDTO представляющих уведомления пользователя
     * @throws EntityNotFoundException если пользователь не найден
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
     * С фильтрацией организатор / участник. Выбирает все мероприятия, где пользователь является участником.
     *
     * @param userId - идентификатор пользователя
     * @param filterType тип фильтра (USER_ORGANIZER, USER_PARTICIPANT, ALL_EVENTS)
     * @return список объектов ViewEventDTO представляющих мероприятия на которые
     * пользователь точно идет
     */
    @Transactional(readOnly = true)
    public List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(Long userId, ParticipantFilterType filterType) {
        List<ViewEventDTO> userEvents = new ArrayList<>();

        switch (filterType) {
            case USER_ORGANIZER:
                List<Event> organizerEvents = eventRepository.findByUserId(userId);
                userEvents.addAll(organizerEvents.stream()
                        .map(eventMapper::toDTO)
                        .collect(Collectors.toList())
                ); break;
            case USER_PARTICIPANT:
                List<NotificationParticipant> participantEvents = notificationParticipantRepository
                        .findByUserIdAndFeedbackType(userId, FeedbackType.APPROVED);

                userEvents.addAll(participantEvents.stream()
                        .map(participant -> eventMapper.toDTO(participant.getEvent()))
                        .collect(Collectors.toList())
                ); break;
            case ALL_EVENTS:
            default:
                return findUserEventsWithApprovedFeedbackAndOrganizer(userId);
        }




        userEvents.sort(Comparator.comparing(ViewEventDTO::getDate));

        log.info("Method *** findUserEventsWithApprovedFeedbackAndOrganizer *** : user ID = {}, filterType = {}," +
                " List<ViewEventDTO> size = {}", userId, filterType, userEvents.size());
        return userEvents;
    }

    /**
     * Выбирает все мероприятия, где пользователь является участником.
     *
     * @param userId - идентификатор пользователя
     * @return список объектов ViewEventDTO представляющих мероприятия на которые
     *пользователь точно идет
     */
    @Transactional(readOnly = true)
    public List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(Long userId) {
        List<ViewEventDTO> userEvents = new ArrayList<>();
        List<Event> allEvents = eventRepository.findByUserId(userId);
        List<NotificationParticipant> allParticipantEvents = notificationParticipantRepository
                .findByUserIdAndFeedbackType(userId, FeedbackType.APPROVED);

        userEvents.addAll(allEvents.stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList())
        );

        userEvents.addAll(allParticipantEvents.stream()
                .map(participant -> eventMapper.toDTO(participant.getEvent()))
                .collect(Collectors.toList())
        );

        log.info("Method *** findUserEventsWithApprovedFeedbackAndOrganizer *** : user ID = {} List<ViewEventDTO> size = {}",
                userId, userEvents.size());
        return userEvents;
    }

    /**
     * Выбирает все мероприятия, где пользователь ожидает одобрения заявки от организатора.
     *
     * @param userId - идентификатор пользователя
     * @return список объектов ViewEventDTO представляющих мероприятия, где пользователь ожидает одобрения заявки
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
     * Выбирает всех участников одного мероприятия, включая организатора.
     *
     * @param eventId - идентификатор мероприятия
     * @return список объектов ViewUserSlimDTO представляющих основную информацию об участниках мероприятия
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

    /**
     * Обновляет флаг просмотра заявки организатором.
     *
     * @param requestId     идентификатор заявки
     * @param ownerViewed   значение флага просмотра
     * @throws EntityNotFoundException, если заявка не найдена
     */
    @Transactional
    public void updateOwnerViewed(Long requestId, boolean ownerViewed) {
        NotificationParticipant notificationParticipant = notificationParticipantRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + requestId));

        notificationParticipant.setOwnerViewed(ownerViewed);
        notificationParticipantRepository.save(notificationParticipant);
        log.info("Method *** /updateOwnerViewed/ *** : notification with ID = {}, ownerViewed = {}", requestId, ownerViewed);
    }

    /**
     * Обновляет флаг просмотра заявки пользователем.
     *
     * @param requestId - идентификатор заявки
     * @param participantViewed - значение флага просмотра
     * @throws EntityNotFoundException, если заявка не найдена
     */
    @Transactional
    public void updateParticipantViewed(Long requestId, boolean participantViewed) {
        NotificationParticipant notificationParticipant = notificationParticipantRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with ID: " + requestId));

        notificationParticipant.setParticipantViewed(participantViewed);
        notificationParticipantRepository.save(notificationParticipant);
        log.info("Method *** updateParticipantViewed *** : notification with ID = {}, participantViewed = {}", requestId, participantViewed);
    }

    /**
     * Проверяет существование уведомления.
     *
     * @param notificationDTO объект с eventId и userId
     * @return true, если уведомление существует, false - не существует
     */
    @Transactional(readOnly = true)
    public boolean isExistNotification(NotificationDTO notificationDTO) {
        Long eventId = notificationDTO.getEventId();
        Long userId = notificationDTO.getUserId();

        log.info("Method *** isExistNotification *** : userId = {} eventId = {}", userId, eventId);
        return notificationParticipantRepository.existsByEventIdAndUserId(eventId, userId);
    }

    /**
     * Ищет заявку участника для конкретного мероприятия.
     *
     * @param eventId - идентификатор мероприятия
     * @param userId - идентификатор пользователя
     * @return объект ViewNotificationDTO представляющий заявку участника
     * @throws EntityNotFoundException если заявка не найдена
     */
    @Transactional(readOnly = true)
    public ViewNotificationDTO findNotificationByUserIdEventId(Long eventId, Long userId) {

        NotificationParticipant notificationParticipant = notificationParticipantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found for event ID: " + eventId + " and user ID: " + userId));

        log.info("Method *** findNotificationByUserIdEventId *** : userId = {} eventId = {}", userId, eventId);

        return notificationMapper.toDTO(notificationParticipant);
    }
}
