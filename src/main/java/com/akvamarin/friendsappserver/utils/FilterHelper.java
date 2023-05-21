package com.akvamarin.friendsappserver.utils;

import com.akvamarin.friendsappserver.domain.dto.request.CurrentUserCoords;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.akvamarin.friendsappserver.domain.enums.SortingType;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;

public class FilterHelper {
    public static boolean matchesCity(Event event, Long cityId) {
        User userOwner = event.getUser();

        if (userOwner != null) {
            City userCity = userOwner.getCity();
            return userCity != null && userCity.getId().equals(cityId);
        }

        return false;
    }

    public static boolean matchesCategories(Event event, List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return true; // no selected category
        }

        Long eventCategoryId = event.getEventCategory().getId();
        return categoryIds.contains(eventCategoryId);
    }

    public static boolean matchesUserOrganizer(Event event, boolean isUserOrganizer, Long currentUserId) {
        if (isUserOrganizer) {
            User eventUser = event.getUser();
            return eventUser != null && eventUser.getId().equals(currentUserId);
        }

        return true; // no use filter
    }

    public static boolean matchesPartner(Event event, List<Partner> partnerList) {
        Partner eventPartner = event.getPartner();

        if (eventPartner == null) {
            return true; // select all partners
        }

        return partnerList.contains(eventPartner);
    }

    public static boolean matchesDaysOfWeek(Event event, List<DayOfWeek> daysOfWeekList) {
        if (daysOfWeekList == null) {
            return true; // select all days of the week
        }

        DayOfWeek eventDayOfWeek = event.getDate().getDayOfWeek();
        return daysOfWeekList.contains(eventDayOfWeek);
    }

    public static boolean matchesPeriodOfTime(Event event, List<PeriodOfTime> periodOfTimeList) {
        if (periodOfTimeList == null) {
            return true; // select all period of time
        }

        PeriodOfTime eventPeriodOfTime = event.getPeriodOfTime();
        return periodOfTimeList.contains(eventPeriodOfTime);
    }

    public static Comparator<Event> getEventComparator(SortingType sortingType, CurrentUserCoords userCoords) {
        switch (sortingType) {
            case CREATION_DATE:
                return Comparator.comparing(Event::getCreatedAt);

            case EVENT_DATE:
                return Comparator.comparing(Event::getDate);

            case CLOSEST_LOCATION:
                return Comparator.comparingDouble(event -> calculateDistance(event, userCoords));

            case CLOSEST_LOCATION_AND_CREATION_DATE:
                return Comparator.comparingDouble((Event event) -> calculateDistance(event, userCoords))
                        .thenComparing(Event::getCreatedAt);

            case CLOSEST_LOCATION_AND_EVENT_DATE:
                return Comparator.comparingDouble((Event event) -> calculateDistance(event, userCoords))
                        .thenComparing(Event::getDate);

            default:
                throw new IllegalArgumentException("Invalid sorting type: " + sortingType);
        }
    }

    private static double calculateDistance(Event event, CurrentUserCoords userCoords) {
        double lat1 = event.getLat();
        double lon1 = event.getLon();
        double lat2 = userCoords.getLat();
        double lon2 = userCoords.getLon();

        // Radius of the Earth in kilometers
        double earthRadius = 6371;

        // Convert coordinates to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the difference between the coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
}
