package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationFeedbackDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.services.NotificationParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationParticipantController {

    private final NotificationParticipantService notificationParticipantService;


    @Operation(
            summary = "Create participant request",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Participant request created", content = @Content(schema = @Schema(implementation = NotificationParticipant.class))),
                    @ApiResponse(responseCode = "404", description = "Event or User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createParticipantRequest(@RequestBody NotificationDTO notificationDTO) {
        final NotificationParticipant notificationParticipant = notificationParticipantService.createParticipantRequest(notificationDTO);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(notificationParticipant.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Update feedback status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Feedback status updated", content = @Content(schema = @Schema(implementation = ViewNotificationDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewNotificationDTO updateFeedbackStatus(@RequestBody NotificationFeedbackDTO notificationFeedbackDTO) {
        return notificationParticipantService.updateFeedbackStatus(notificationFeedbackDTO);
    }

    @Operation(
            summary = "Delete participant request",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Participant request deleted"),
                    @ApiResponse(responseCode = "404", description = "Participant request not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParticipantRequest(@PathVariable Long requestId) {
        notificationParticipantService.deleteParticipantRequest(requestId);
    }

    @Operation(
            summary = "Find all notifications by user and org ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notifications found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewNotificationDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ViewNotificationDTO> findAllNotificationsByUserId(@PathVariable Long userId) {
        return notificationParticipantService.findNotificationsByUserId(userId);
    }


    @Operation(
            summary = "Find user events with approved feedback and organizer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User events found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewEventDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/participant/{userId}")
    public List<ViewEventDTO> findParticipantWithApproved(@PathVariable Long userId) {
        return notificationParticipantService.findUserEventsWithApprovedFeedbackAndOrganizer(userId);
    }

    @Operation(
            summary = "Find user events with waiting feedback",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User events found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewEventDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/user/{userId}/waiting")
    public List<ViewEventDTO> findUserEventsWithWaitingFeedback(@PathVariable Long userId) {
        return notificationParticipantService.findUserEventsWithWaitingFeedback(userId);
    }

    @Operation(
            summary = "Find event participants with approved feedback",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event participants found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewUserSlimDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/event/{eventId}/participants")
    public List<ViewUserSlimDTO> findEventParticipantsWithApprovedFeedback(@PathVariable Long eventId) {
        return notificationParticipantService.findEventParticipantsWithApprovedFeedback(eventId);
    }

    @Operation(
            summary = "Update ownerViewed field",
            responses = {
                    @ApiResponse(responseCode = "204", description = "OwnerViewed field updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/{requestId}/owner-viewed")
    public void updateOwnerViewed(@PathVariable Long requestId, @RequestParam boolean ownerViewed) {
        notificationParticipantService.updateOwnerViewed(requestId, ownerViewed);
    }

    @Operation(
            summary = "Update participantViewed field",
            responses = {
                    @ApiResponse(responseCode = "204", description = "ParticipantViewed field updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/{requestId}/participant-viewed")
    public void updateParticipantViewed(@PathVariable Long requestId, @RequestParam boolean participantViewed) {
        notificationParticipantService.updateParticipantViewed(requestId, participantViewed);
    }

    @Operation(
            summary = "Notification is exist by eventId and userId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Boolean answer"),
            }
    )
    @GetMapping("/check")
    public ResponseEntity<Boolean> isExistNotification(NotificationDTO notificationDTO) {
        boolean isExist = notificationParticipantService.isExistNotification(notificationDTO);
        return ResponseEntity.ok(isExist);
    }

    @Operation(
            summary = "Find notification by eventId and userId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Notification found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewNotificationDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Notification not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/event/{eventId}/participant/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ViewNotificationDTO findNotificationByUserIdEventId(@PathVariable Long eventId, @PathVariable Long userId) {
        return notificationParticipantService.findNotificationByUserIdEventId(eventId, userId);
    }
}

