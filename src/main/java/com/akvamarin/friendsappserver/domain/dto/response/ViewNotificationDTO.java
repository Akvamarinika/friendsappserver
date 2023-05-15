package com.akvamarin.friendsappserver.domain.dto.response;

import com.akvamarin.friendsappserver.domain.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewNotificationDTO {

    private Long id;

    private long eventId;

    private String eventName;

    private long userId;

    private String userNickname;

    private String userUrlAvatar;

    private FeedbackType feedbackType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean forOwner;

}
