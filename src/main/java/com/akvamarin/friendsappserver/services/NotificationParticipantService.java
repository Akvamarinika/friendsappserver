package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.request.NotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationFeedbackDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.enums.ParticipantFilterType;

import java.util.List;

public interface NotificationParticipantService {
    List<ViewNotificationDTO> findNotificationsByUserId(Long userId);

    NotificationParticipant createParticipantRequest(NotificationDTO notificationDTO);

    ViewNotificationDTO updateFeedbackStatus(NotificationFeedbackDTO notificationFeedbackDTO);

    void deleteParticipantRequest(Long requestId);

    List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(Long userId);

    List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(Long userId, ParticipantFilterType filterType);

    List<ViewEventDTO> findUserEventsWithWaitingFeedback(Long userId);

    List<ViewUserSlimDTO> findEventParticipantsWithApprovedFeedback(Long eventId);

    void updateOwnerViewed(Long requestId, boolean ownerViewed);

    void updateParticipantViewed(Long requestId, boolean participantViewed);

    boolean isExistNotification(NotificationDTO notificationDTO);

    ViewNotificationDTO findNotificationByUserIdEventId(Long eventId, Long userId);

}
