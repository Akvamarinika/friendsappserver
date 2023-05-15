package com.akvamarin.friendsappserver.domain.dto.request;

import com.akvamarin.friendsappserver.domain.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationFeedbackDTO {

    @NotNull
    private Long id;

    @NotNull
    private FeedbackType feedbackType;
}
