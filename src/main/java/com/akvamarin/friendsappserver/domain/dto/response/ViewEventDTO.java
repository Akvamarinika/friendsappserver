package com.akvamarin.friendsappserver.domain.dto.response;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для отображения
 * мероприятия для других пользователей
 * */
public class ViewEventDTO {
    private Long id;

    private String name;

    private String description;

    private LocalDate date;

    private PeriodOfTime periodOfTime;

    private Partner partner;

    private String categoryName;

    private ViewUserSlimDTO userOwner;


    private Set<EventCategoryDTO> eventCategories;
}
