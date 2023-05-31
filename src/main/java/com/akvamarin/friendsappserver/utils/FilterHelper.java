package com.akvamarin.friendsappserver.utils;

import com.akvamarin.friendsappserver.domain.dto.request.CurrentUserCoords;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.akvamarin.friendsappserver.domain.enums.SortingType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class FilterHelper {
    public static boolean matchesCity(Event event, Long cityId) {
        User userOwner = event.getUser();

        if (userOwner != null && cityId != null) {
            City userCity = userOwner.getCity();
            return userCity != null && userCity.getId().equals(cityId);
        }

        return cityId == null; // no selected city
    }


    public static boolean matchesCategories(Event event, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return true; // no selected category
        }

        EventCategory eventCategory = event.getEventCategory();
        return eventCategory != null && categoryIds.contains(eventCategory.getId());
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

        if (eventPartner == null || partnerList == null) {
            return true; // select all partners
        }

        return partnerList.contains(eventPartner);
    }

    public static boolean matchesDaysOfWeek(Event event, List<DayOfWeek> daysOfWeekList) {
        if (daysOfWeekList == null) {
            return true; // select all days of the week
        }

        LocalDate eventDate = event.getDate();
        return eventDate != null && daysOfWeekList.contains(eventDate.getDayOfWeek());
    }

    public static boolean matchesPeriodOfTime(Event event, List<PeriodOfTime> periodOfTimeList) {
        if (periodOfTimeList == null) {
            return true; // select all period of time
        }

        PeriodOfTime eventPeriodOfTime = event.getPeriodOfTime();
        return eventPeriodOfTime != null && periodOfTimeList.contains(eventPeriodOfTime);
    }

    public static Comparator<Event> getEventComparator(SortingType sortingType, CurrentUserCoords userCoords) {
        if (sortingType == null || sortingType == SortingType.CREATION_DATE) {
            return Comparator.comparing(Event::getCreatedAt).reversed();
        }

        switch (sortingType) {
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
        if (event == null || userCoords == null) {
            throw new IllegalArgumentException("Event or userCoords is null.");
        }

        double lat1 = event.getLat();
        double lon1 = event.getLon();
        double lat2 = userCoords.getLat();
        double lon2 = userCoords.getLon();

        // radius of the Earth in kilometers
        double earthRadius = 6371;

        // convert coordinates to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // calculate difference between coordinates
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        return distance;
    }
}

