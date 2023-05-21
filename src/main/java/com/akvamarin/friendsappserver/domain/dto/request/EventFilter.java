package com.akvamarin.friendsappserver.domain.dto.request;

import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.akvamarin.friendsappserver.domain.enums.SortingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.time.DayOfWeek;
import java.util.List;

@Builder
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventFilter {
    private Long cityId;

    private List<Long> categoryIds;

    private boolean isUserOrganizer;

    private List<Partner> partnerList;

    private List<DayOfWeek> daysOfWeekList;

    private List<PeriodOfTime> periodOfTimeList;

    private SortingType sortingType;

    private Long currentUserId;

    private CurrentUserCoords userCoords;
}
