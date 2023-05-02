package com.akvamarin.friendsappserver.domain.dto.request;

import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
/**
 * DTO для создания / обновления
 * удаления мероприятия
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder @Jacksonized
public class EventDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDate date;

    private PeriodOfTime periodOfTime;

    private Partner partner;

    private Long eventCategoryId;

    private Long ownerId;

    private Double lat;

    private Double lon;

}
