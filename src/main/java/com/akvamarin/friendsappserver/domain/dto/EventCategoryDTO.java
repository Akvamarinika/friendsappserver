package com.akvamarin.friendsappserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCategoryDTO {
    private Long id;
    private String name;
    private String urlIcon;

}
