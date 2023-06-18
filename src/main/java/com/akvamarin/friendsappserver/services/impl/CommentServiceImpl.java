package com.akvamarin.friendsappserver.services.impl;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис по работе с комментариями пользователей, оставленных
 * к мероприятиям, взаимодействует с CommentRepository, EventRepository, UserRepository.
 *
 * @see Event
 * @see CommentRepository
 * @see EventRepository
 * @see UserRepository
 * @see CommentMapper
 * */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    /**
     * Создает комментарий для мероприятия.
     *
     * @param eventId - идентификатор мероприятия
     * @param commentDTO - комментарий пользователя DTO
     * @return созданный комментарий
     * @throws EntityNotFoundException, если мероприятие или пользователь не найдены
     */
    @Transactional
    @Override
    public CommentDTO createComment(Long eventId, CommentDTO commentDTO) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new EntityNotFoundException("Event not found"));

        Optional<User> optionalUser = userRepository.findById(commentDTO.getUserId());
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setEvent(event);
        comment.setUser(user);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toDTO(savedComment);
    }

    /**
     * Возвращает комментарий по его идентификатору.
     *
     * @param commentId - идентификатор комментария
     * @return комментарий DTO
     * @throws EntityNotFoundException, если комментарий не найден
     */
    @Transactional
    @Override
    public ViewCommentDTO findCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));
        return commentMapper.toViewDTO(comment);
    }

    /**
     * Возвращает список всех комментариев для указанного мероприятия.
     *
     * @param eventId - идентификатор мероприятия
     * @return список комментариев DTO
     * @throws EntityNotFoundException, если мероприятие не найдено
     */
    @Transactional
    @Override
    public List<ViewCommentDTO> getAllCommentsByEventId(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Event event = optionalEvent.orElseThrow(() -> new EntityNotFoundException("Event not found"));

        List<Comment> comments = commentRepository.findAllByEventOrderByCreatedAtDesc(event);
        return comments.stream().map(commentMapper::toViewDTO).collect(Collectors.toList());
    }

    /**
     * Возвращает список всех комментариев для указанного пользователя.
     *
     * @param userId - идентификатор пользователя
     * @return список комментариев DTO
     */
    @Transactional
    @Override
    public List<CommentDTO> getAllCommentsForUser(Long userId) {
        List<Comment> comments = commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return comments.stream()
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId - идентификатор комментария
     */
    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    /**
     * Обновляет комментарий по его идентификатору.
     *
     * @param commentId - идентификатор комментария
     * @param commentDTO - комментарий DTO для обновления
     * @return обновленный комментарий
     * @throws EntityNotFoundException, если комментарий не найден
     */
    @Transactional
    @Override
    public ViewCommentDTO updateComment(Long commentId, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id " + commentId + " not found"));

        comment.setText(commentDTO.getText());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setEdited(true);

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toViewDTO(updatedComment);
    }
}
