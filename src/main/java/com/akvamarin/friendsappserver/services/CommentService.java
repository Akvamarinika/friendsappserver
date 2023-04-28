package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long eventId, CommentDTO commentDTO);
    List<CommentDTO> getAllCommentsByEventId(Long eventId);

    List<CommentDTO> getAllCommentsForUser(Long userId);

    CommentDTO findCommentById(Long commentId);

    void deleteComment(Long commentId);

    CommentDTO updateComment(Long commentId, CommentDTO commentDTO);
}
