package com.akvamarin.friendsappserver.controllers;

import com.akvamarin.friendsappserver.domain.dto.error.ErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.error.ValidationErrorResponse;
import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.services.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @Operation(
            summary = "Create new comment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "New comment created", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Event or user not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/create/{eventId}")
    public ResponseEntity<Void> createComment(@PathVariable Long eventId,
                                              @Valid @RequestBody CommentDTO commentDTO) {
        final CommentDTO savedComment = commentService.createComment(eventId, commentDTO);

        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Find comment by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment found", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> findCommentById(@PathVariable Long commentId) {
        CommentDTO comment = commentService.findCommentById(commentId);
        return ResponseEntity.ok(comment);
    }

    @Operation(
            summary = "Get all comments for an event",
            responses = @ApiResponse(responseCode = "200", description = "All comments for the event", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class))))
    )
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByEventId(@PathVariable Long eventId) {
        List<CommentDTO> comments = commentService.getAllCommentsByEventId(eventId);
        return ResponseEntity.ok(comments);
    }

    @Operation(
            summary = "Get all comments for a user",
            responses = @ApiResponse(responseCode = "200", description = "All comments for the user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class))))
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForUser(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getAllCommentsForUser(userId);
        return ResponseEntity.ok(comments);
    }

    @Operation(
            summary = "Delete comment by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comment for requested ID is removed"),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update existing comment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment for requested ID is updated", content = @Content(schema = @Schema(implementation = CommentDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Wrong request format", content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId,
                                                    @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }
}
