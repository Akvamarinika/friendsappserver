package com.akvamarin.friendsappserver.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long userId;

    private Long eventId;
}
