package com.akvamarin.friendsappserver.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewCommentDTO {
    private Long id;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private ViewUserSlimDTO userSlimDTO;

    private Long eventId;

    @JsonProperty(value = "edited")
    private boolean edited;
}
