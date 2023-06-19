package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.error.ValidationErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.request.EventDTO;
import com.akvamarin.friendsappserver.domain.dto.request.EventFilter;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventUpdateDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.security.CurrentUser;
import com.akvamarin.friendsappserver.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Контроллер для работы с мероприятиями,
 * взаимодействует с EventService
 *
 * @see EventService
 * */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventRestController {

    private final EventService eventService;

    /**
     * Создает новое мероприятие.
     *
     * @param eventDTO - мероприятие DTO
     * @param currentUser - текущий пользователь
     * @return ответ с кодом состояния 201 (Created)
     */
    @Operation(
            summary = "Create new event",
            responses = {
                    @ApiResponse(responseCode = "201", description = "New event is created"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(name = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventDTO eventDTO, @Parameter(hidden = true) @CurrentUser User currentUser) {
        eventDTO.setOwnerId(currentUser.getId());
        log.debug("currentUser ID" + currentUser.getId());
        final Event createdEvent = eventService.createNewEvent(eventDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEvent.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    /**
     * Возвращает список всех мероприятий.
     *
     * @return список мероприятий DTO
     */
    @Operation(
            summary = "Get all events",
            responses = @ApiResponse(responseCode = "200",
                    description = "All events",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDTO.class))))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ViewEventDTO> getAllEvents() {
        return eventService.findAll();
    }

    /**
     * Находит мероприятие по его идентификатору.
     *
     * @param id - идентификатор мероприятия
     * @return мероприятие DTO
     */
    @Operation(
            summary = "Find event by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event by ID found", content = @Content(schema = @Schema(implementation = EventDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewEventDTO getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    /**
     * Находит мероприятие по его идентификатору вместе со всеми категориями.
     *
     * @param id - идентификатор мероприятия
     * @return мероприятие DTO с категориями
     */
    @Operation(
            summary = "Find event by ID, with all categories",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event by ID found", content = @Content(schema = @Schema(implementation = EventDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewEventUpdateDTO getEventByIdWithCategories(@PathVariable Long id) {
        return eventService.findByIdForViewUpdate(id);
    }

    /**
     * Обновляет существующее мероприятие.
     *
     * @param dto   мероприятие DTO для обновления
     * @return обновленное мероприятие DTO
     */
    @Operation(
            summary = "Update existing event",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event for requested ID is updated", content = @Content(schema = @Schema(implementation = EventDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewEventDTO updateEvent(@Valid @RequestBody EventDTO dto) {
        return eventService.updateEvent(dto);
    }

    /**
     * Удаляет мероприятие по его идентификатору.
     *
     * @param id - идентификатор мероприятия
     */
    @Operation(
            summary = "Remove event by ID",
            responses = @ApiResponse(responseCode = "204", description = "Event for requested ID is removed")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
    }

    /**
     * Фильтрует мероприятия в соответствии с заданными параметрами фильтра.
     *
     * @param eventFilter - фильтр мероприятий
     * @return список отфильтрованных мероприятий DTO
     */
    @Operation(
            summary = "Filter events",
            responses = @ApiResponse(responseCode = "200",
                    description = "Filtered events",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewEventDTO.class))))
    )
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ViewEventDTO> filterEvents(@RequestBody EventFilter eventFilter) {
        log.info("Method *** filterEvents *** : eventFilter = {} ", eventFilter);
        return eventService.filterEvents(eventFilter);
    }
}

