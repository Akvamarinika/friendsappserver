package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.request.NotificationFeedbackDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewEventDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewNotificationDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewUserSlimDTO;
import com.akvamarin.friendsappserver.domain.entity.event.NotificationParticipant;
import com.akvamarin.friendsappserver.domain.enums.ParticipantFilterType;
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

/**
 * Контроллер по работе с заявками на мероприятия,
 * уведомлениями и участниками, взаимодействует с NotificationParticipantService.
 *
 * @see NotificationParticipantService
 * */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationParticipantController {

    private final NotificationParticipantService notificationParticipantService;


    /**
     * Создает запрос-заявку на участие.
     *
     * @param notificationDTO - уведомление DTO (заявка)
     * @return ответ без содержимого
     */
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

    /**
     * Обновляет статус заявки.
     *
     * @param notificationFeedbackDTO - уведомление содержит тип статуса заявки DTO и ID заявки
     * @return DTO уведомления с обновленным статусом обратной связи
     */
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

    /**
     * Удаляет запрос на участие.
     *
     * @param requestId - идентификатор заявки на участие
     */
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

    /**
     * Находит все заявки по идентификатору пользователя и организатора.
     *
     * @param userId - идентификатор пользователя
     * @return список заявок DTO
     */
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

    /**
     * С фильтрацией организатор / участник. Выбирает все мероприятия, где пользователь является участником.
     *
     * @param userId - идентификатор пользователя
     * @param filterType - тип фильтра (USER_ORGANIZER, USER_PARTICIPANT, ALL_EVENTS)
     * @return список мероприятий DTO на которые пользователь точно идет
     * (включая, где пользователь организатор, либо нет. В зависимости от фильтра)
     */
    @Operation(
            summary = "Find user events with approved feedback and organizer (with filtering)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Event participants found", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ViewUserSlimDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/participant/{userId}/filter")
    public List<ViewEventDTO> findUserEventsWithApprovedFeedbackAndOrganizer(
            @PathVariable Long userId,
            @RequestParam ParticipantFilterType filterType) {
        return notificationParticipantService.findUserEventsWithApprovedFeedbackAndOrganizer(userId, filterType);
    }

    /**
     * Выбирает все мероприятия, где пользователь является участником.
     *
     * @param userId - идентификатор пользователя
     * @return список мероприятий DTO на которые пользователь точно идет
     */
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

    /**
     * Выбирает все мероприятия, где пользователь ожидает одобрения заявки от организатора.
     *
     * @param userId - идентификатор пользователя
     * @return список мероприятий DTO
     */
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

    /**
     * Выбирает всех участников одного мероприятия, включая организатора.
     *
     * @param eventId - идентификатор события
     * @return список участников мероприятия DTO
     */
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

    /**
     * Обновляет поле ownerViewed (просмотр заявки организатором).
     *
     * @param requestId - идентификатор заявки
     * @param ownerViewed - флаг просмотра организатором
     */
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

    /**
     * Обновляет поле participantViewed (просмотр уведомления участником).
     *
     * @param requestId - идентификатор заявки
     * @param participantViewed  флаг просмотра участником
     */
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

    /**
     * Проверяет существование заявки по идентификатору мероприятия и пользователя.
     *
     * @param notificationDTO уведомление DTO (заявка)
     * @return ответ с булевым значением
     */
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

    /**
     * Находит заявку по идентификатору события и пользователя.
     *
     * @param eventId - идентификатор мероприятия
     * @param userId - идентификатор пользователя
     * @return заявка DTO
     */
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

