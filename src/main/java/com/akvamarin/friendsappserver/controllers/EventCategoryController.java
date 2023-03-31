package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.EventCategoryDTO;
import com.akvamarin.friendsappserver.domain.dto.responseerror.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.responseerror.ValidationErrorResponse;
import com.akvamarin.friendsappserver.services.EventCategoryService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("api/v1/eventCategories")
@RequiredArgsConstructor
@Slf4j
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    @Operation(
            summary = "Create new event category",
            responses = {
                    @ApiResponse(responseCode = "201", description = "New event category is created"),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class)))
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventCategoryDTO> createEventCategory(@Valid @RequestBody EventCategoryDTO eventCategoryDTO) {

        final EventCategoryDTO createdEventCategoryDTO = eventCategoryService.createNew(eventCategoryDTO);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEventCategoryDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdEventCategoryDTO);
    }

    @Operation(
            summary = "Get all event categories",
            responses = @ApiResponse(responseCode = "200",
                    description = "All event categories",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventCategoryDTO.class))))
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventCategoryDTO> getAllEventCategories() {
        return eventCategoryService.findAll();
    }

    @Operation(
            summary = "Find event category by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event category by ID found", content = @Content(schema = @Schema(implementation = EventCategoryDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Event category not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EventCategoryDTO getEventCategoryByID(@PathVariable Long id) {
        return eventCategoryService.findById(id);
    }

    @Operation(
            summary = "Update existing event category",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event category for requested ID is updated", content = @Content(schema = @Schema(implementation = EventCategoryDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Requested data not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public EventCategoryDTO updateEventCategory(@Valid @RequestBody EventCategoryDTO dto) {
        return eventCategoryService.update(dto);
    }

    @Operation(
            summary = "Remove event category by ID",
            responses = @ApiResponse(responseCode = "204", description = "Event category for requested ID is removed")
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEventCategory(@PathVariable Long id) {
        eventCategoryService.deleteById(id);
    }
}
