package com.akvamarin.friendsappserver.domain.dto.response;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO для отображения
 * мероприятия для других пользователей
 * + обновление мероприятия (for response)
 * */

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewEventUpdateDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDate date;

    private PeriodOfTime periodOfTime;

    private Partner partner;

    private EventCategoryDTO eventCategory;

    private ViewUserSlimDTO userOwner;

    private Double lat;

    private Double lon;

    @JsonProperty("allEventCategories")
    private List<EventCategoryDTO> eventCategories;
}
