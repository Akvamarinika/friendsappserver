package com.akvamarin.friendsappserver.integrationtest;

import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.akvamarin.friendsappserver.domain.enums.PeriodOfTime;
import com.akvamarin.friendsappserver.services.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventRestControllerTest {
    private static final String EVENTS_ENDPOINT = "/api/v1/events";
    private static final String EVENT_ENDPOINT = "/api/v1/events/{id}";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventService eventService;

    private EventDTO eventDTO;

    @BeforeEach
    void setUp() {
        eventDTO = createEventDTO();
    }

    @AfterEach
    void tearDown() {
        eventService.deleteAll();
    }

    @Test
    void testGetAllEvents() {
        List<EventDTO> eventDTOs = createMultipleEventDTOs();
        eventService.saveAll(eventDTOs);

        ResponseEntity<EventDTO[]> response = restTemplate.getForEntity(EVENTS_ENDPOINT, EventDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventDTOs.size(), response.getBody().length);
    }


    @Test
    void testCreateEvent() {
        ResponseEntity<EventDTO> response = restTemplate.postForEntity(EVENTS_ENDPOINT + "/createEvent", eventDTO, EventDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(eventDTO.getName(), response.getBody().getName());
        assertEquals(eventDTO.getDescription(), response.getBody().getDescription());
        assertEquals(eventDTO.getDate(), response.getBody().getDate());
        assertEquals(eventDTO.getPeriodOfTime(), response.getBody().getPeriodOfTime());
        assertEquals(eventDTO.getPartner(), response.getBody().getPartner());
        assertEquals(eventDTO.getEventCategoryId(), response.getBody().getEventCategoryId());
        assertEquals(eventDTO.getOwnerId(), response.getBody().getOwnerId());
    }

    @Test
    void testGetEventById() {
        Event savedEventDTO = eventService.createNewEvent(eventDTO);

        ResponseEntity<Event> response = restTemplate.getForEntity(EVENT_ENDPOINT, Event.class, savedEventDTO.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventDTO.getName(), response.getBody().getName());
        assertEquals(eventDTO.getDescription(), response.getBody().getDescription());
        assertEquals(eventDTO.getDate(), response.getBody().getDate());
        assertEquals(eventDTO.getPeriodOfTime(), response.getBody().getPeriodOfTime());
        assertEquals(eventDTO.getPartner(), response.getBody().getPartner());
        assertEquals(eventDTO.getEventCategoryId(), response.getBody().getEventCategory().getId());
        assertEquals(eventDTO.getOwnerId(), response.getBody().getUser().getId());
    }

    @Test
    void testUpdateEvent() {
        Event savedEvent = eventService.createNewEvent(eventDTO);
        savedEvent.setName("Updated Name");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Event> requestEntity = new HttpEntity<>(savedEvent, headers);

        ResponseEntity<ViewEventDTO> response = restTemplate.exchange(EVENTS_ENDPOINT, HttpMethod.PATCH, requestEntity, ViewEventDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(savedEvent.getName(), response.getBody().getName());
        assertEquals(eventDTO.getDescription(), response.getBody().getDescription());
        assertEquals(eventDTO.getDate(), response.getBody().getDate());
        assertEquals(eventDTO.getPeriodOfTime(), response.getBody().getPeriodOfTime());
        assertEquals(eventDTO.getPartner(), response.getBody().getPartner());
        assertEquals(eventDTO.getEventCategoryId(), response.getBody().getEventCategory().getId());
        assertEquals(eventDTO.getOwnerId(), response.getBody().getUserOwner().getId());
    }

    /* @Test
     void testDeleteEvent() {
         EventDTO savedEventDTO = eventService.createNewEvent(eventDTO);

         ResponseEntity<Void> response = restTemplate.exchange(EVENT_ENDPOINT, HttpMethod.DELETE, null, Void.class, savedEventDTO.getId());
 */
    @Test
    void testDeleteEvent() {
        // Create a new event and save it to the database
        EventDTO eventDTO = createEventDTO();
        Event savedEventDTO = eventService.createNewEvent(eventDTO);

        // Delete the event
        restTemplate.delete(EVENT_ENDPOINT, savedEventDTO.getId());

        // Verify that the event was deleted by trying to retrieve it
        ResponseEntity<ViewEventDTO> response = restTemplate.getForEntity(EVENT_ENDPOINT, ViewEventDTO.class, savedEventDTO.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private EventDTO createEventDTO() {
        return EventDTO.builder()
                .name("Test Event")
                .description("This is a test event.")
                .date(LocalDate.now())
                .periodOfTime(PeriodOfTime.EVENING)
                .partner(Partner.MAN)
                .eventCategoryId(1L)
                .ownerId(1L)
                .build();
    }

    private List<EventDTO> createMultipleEventDTOs() {
        List<EventDTO> eventDTOs = new ArrayList<>();
        eventDTOs.add(EventDTO.builder()
                .id(1L)
                .name("Event 1")
                .description("Description for Event 1")
                .date(LocalDate.now().plusDays(1))
                .periodOfTime(PeriodOfTime.MORNING)
                .partner(Partner.COMPANY)
                .eventCategoryId(1L)
                .ownerId(1L)
                .build());

        eventDTOs.add(EventDTO.builder()
                .id(2L)
                .name("Event 2")
                .description("Description for Event 2")
                .date(LocalDate.now().plusDays(2))
                .periodOfTime(PeriodOfTime.AFTERNOON)
                .partner(Partner.ANY)
                .eventCategoryId(2L)
                .ownerId(2L)
                .build());
        return eventDTOs;
    }

}