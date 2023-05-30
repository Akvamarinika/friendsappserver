package com.akvamarin.friendsappserver.unittest;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewCommentDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.message.Comment;
import com.akvamarin.friendsappserver.domain.mapper.message.CommentMapper;
import com.akvamarin.friendsappserver.repositories.CommentRepository;
import com.akvamarin.friendsappserver.repositories.EventRepository;
import com.akvamarin.friendsappserver.repositories.UserRepository;
import com.akvamarin.friendsappserver.services.CommentService;
import com.akvamarin.friendsappserver.services.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateComment() {
        Long eventId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(1L);
        Event event = new Event();
        User user = new User();
        Comment comment = new Comment();
        Comment savedComment = new Comment();
        CommentDTO expectedCommentDTO = new CommentDTO();

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(userRepository.findById(commentDTO.getUserId())).thenReturn(Optional.of(user));
        Mockito.when(commentMapper.toEntity(commentDTO)).thenReturn(comment);
        Mockito.when(commentRepository.save(comment)).thenReturn(savedComment);
        Mockito.when(commentMapper.toDTO(savedComment)).thenReturn(expectedCommentDTO);

        //create comment
        CommentDTO result = commentService.createComment(eventId, commentDTO);

        Mockito.verify(eventRepository).findById(eventId);
        Mockito.verify(userRepository).findById(commentDTO.getUserId());
        Mockito.verify(commentMapper).toEntity(commentDTO);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(commentMapper).toDTO(savedComment);

        Assertions.assertEquals(expectedCommentDTO, result);
    }

    @Test
    public void testFindCommentById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        ViewCommentDTO expectedCommentDTO = new ViewCommentDTO();

        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        Mockito.when(commentMapper.toViewDTO(comment)).thenReturn(expectedCommentDTO);

        ViewCommentDTO result = commentService.findCommentById(commentId);

        Mockito.verify(commentRepository).findById(commentId);
        Mockito.verify(commentMapper).toViewDTO(comment);

        Assertions.assertEquals(expectedCommentDTO, result);
    }

    @Test
    public void testGetAllCommentsByEventId() {
        Long eventId = 1L;
        Event event = new Event();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2);
        ViewCommentDTO viewCommentDTO1 = new ViewCommentDTO();
        ViewCommentDTO viewCommentDTO2 = new ViewCommentDTO();

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        Mockito.when(commentRepository.findAllByEventOrderByCreatedAtDesc(event)).thenReturn(comments);
        Mockito.when(commentMapper.toViewDTO(comment1)).thenReturn(viewCommentDTO1);
        Mockito.when(commentMapper.toViewDTO(comment2)).thenReturn(viewCommentDTO2);

        List<ViewCommentDTO> result = commentService.getAllCommentsByEventId(eventId);

        Mockito.verify(eventRepository).findById(eventId);
        Mockito.verify(commentRepository).findAllByEventOrderByCreatedAtDesc(event);
        Mockito.verify(commentMapper, Mockito.times(2)).toViewDTO(Mockito.any(Comment.class));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(viewCommentDTO1, result.get(0));
        Assertions.assertEquals(viewCommentDTO2, result.get(1));
    }


    @Test
    public void testGetAllCommentsForUser() {
        Long userId = 1L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2);
        CommentDTO commentDTO1 = new CommentDTO();
        CommentDTO commentDTO2 = new CommentDTO();

        Mockito.when(commentRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(comments);
        Mockito.when(commentMapper.toDTO(comment1)).thenReturn(commentDTO1);
        Mockito.when(commentMapper.toDTO(comment2)).thenReturn(commentDTO2);

        List<CommentDTO> result = commentService.getAllCommentsForUser(userId);

        Mockito.verify(commentRepository).findByUserIdOrderByCreatedAtDesc(userId);
        Mockito.verify(commentMapper, Mockito.times(2)).toDTO(Mockito.any(Comment.class));

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(commentDTO1, result.get(0));
        Assertions.assertEquals(commentDTO2, result.get(1));
    }


    @Test
    public void testDeleteComment() {
        Long commentId = 1L;

        commentService.deleteComment(commentId);

        Mockito.verify(commentRepository).deleteById(commentId);
    }
}

