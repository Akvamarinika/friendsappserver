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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:cleanup.sql")
@Sql(scripts = "classpath:data.sql")
public class EventRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EventService eventService;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/events";
    }

    @AfterEach
    void tearDown() {
        eventService.deleteAll();
    }

    @Test
    void testCreateEvent() {
        EventDTO eventDTO = createEventDTO();

        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, eventDTO, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void testGetAllEvents() {
        EventDTO eventDTO = createEventDTO();
        eventService.createNewEvent(eventDTO);

        ResponseEntity<EventDTO[]> response = restTemplate.getForEntity(baseUrl, EventDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void testGetEventById() {
        EventDTO eventDTO = createEventDTO();
        Event createdEvent = eventService.createNewEvent(eventDTO);

        ResponseEntity<EventDTO> response = restTemplate.getForEntity(baseUrl + "/{id}", EventDTO.class, createdEvent.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdEvent.getName(), response.getBody().getName());
        assertEquals(createdEvent.getDescription(), response.getBody().getDescription());
    }

    @Test
    void testUpdateEvent() {
        EventDTO eventDTO = createEventDTO();
        Event createdEvent = eventService.createNewEvent(eventDTO);
        createdEvent.setName("Updated Name");

        restTemplate.put(baseUrl, createdEvent);

        ViewEventDTO updatedEvent = restTemplate.getForObject(baseUrl + "/{id}", ViewEventDTO.class, createdEvent.getId());
        assertEquals("Updated Name", updatedEvent.getName());
    }


    @Test
    void testDeleteEvent() {
        EventDTO eventDTO = createEventDTO();
        Event createdEvent = eventService.createNewEvent(eventDTO);

        restTemplate.delete(baseUrl + "/{id}", createdEvent.getId());

        assertThrows(EntityNotFoundException.class, () -> eventService.findById(createdEvent.getId()));
    }


    private EventDTO createEventDTO() {
        return EventDTO.builder()
                .name("Test Event")
                .description("This is a test event.")
                .build();
    }
}

