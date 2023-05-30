package com.akvamarin.friendsappserver.integrationtest;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewCommentDTO;
import com.akvamarin.friendsappserver.services.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommentService commentService;

    @LocalServerPort
    private int port;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/comments";
    }

    @AfterEach
    void tearDown() {
        Long eventId = 1L;
        List<ViewCommentDTO> comments = commentService.getAllCommentsByEventId(eventId);

        for (ViewCommentDTO comment : comments) {
            commentService.deleteComment(comment.getId());
        }
    }

    @Test
    void testCreateComment() {
        Long eventId = 1L;
        CommentDTO commentDTO = createCommentDTO();

        //POST request
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/create/{eventId}", commentDTO, Void.class, eventId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
    }

    @Test
    void testFindCommentById() {
        Long commentId = 1L;

        //GET
        ResponseEntity<ViewCommentDTO> response = restTemplate.getForEntity(baseUrl + "/{commentId}", ViewCommentDTO.class, commentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllCommentsByEventId() {
        // Prepare test data
        Long eventId = 1L;

        //GET
        ResponseEntity<ViewCommentDTO[]> response = restTemplate.getForEntity(baseUrl + "/event/{eventId}", ViewCommentDTO[].class, eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length); //no comments for event
    }

    @Test
    void testGetAllCommentsForUser() {
        Long userId = 1L;

        //GET request
        ResponseEntity<CommentDTO[]> response = restTemplate.getForEntity(baseUrl + "/user/{userId}", CommentDTO[].class, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length); //no comments for user
    }

    @Test
    void testDeleteComment() {
        Long commentId = 1L;

        //DELETE
        restTemplate.delete(baseUrl + "/{commentId}", commentId);

        assertThrows(EntityNotFoundException.class, () -> commentService.findCommentById(commentId));
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        CommentDTO commentDTO = createCommentDTO();

        //PATCH update comment
        restTemplate.patchForObject(baseUrl + "/{commentId}", commentDTO, Void.class, commentId);

        // updated comment
        ViewCommentDTO updatedComment = restTemplate.getForObject(baseUrl + "/{commentId}", ViewCommentDTO.class, commentId);
        assertNotNull(updatedComment);
        assertEquals(commentDTO.getText(), updatedComment.getText());
    }

    private CommentDTO createCommentDTO() {
        return CommentDTO.builder()
                .text("Test Comment")
                .userId(1L)
                .build();
    }
}

