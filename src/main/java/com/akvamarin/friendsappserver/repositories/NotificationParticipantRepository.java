package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.enums.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationParticipantRepository extends JpaRepository<NotificationParticipant, Long> {

    List<NotificationParticipant> findByEventIdAndFeedbackType(Long eventId, FeedbackType feedbackType);

    List<NotificationParticipant> findByEventUserAndOwnerViewedFalse(User user);

    List<NotificationParticipant> findByEventUserAndOwnerViewedFalseAndFeedbackTypeIn(User user, List<FeedbackType> asList); // -

    List<NotificationParticipant> findByUserAndParticipantViewedFalseAndFeedbackTypeIn(User user, List<FeedbackType> asList);

    List<NotificationParticipant> findByUserIdAndFeedbackType(Long participantId, FeedbackType approved);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Optional<NotificationParticipant> findByEventIdAndUserId(Long eventId, Long userId);

}
