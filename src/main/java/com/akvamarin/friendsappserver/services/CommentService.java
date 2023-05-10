package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.message.CommentDTO;
import com.akvamarin.friendsappserver.domain.dto.response.ViewCommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long eventId, CommentDTO commentDTO);
    List<ViewCommentDTO> getAllCommentsByEventId(Long eventId);

    List<CommentDTO> getAllCommentsForUser(Long userId);

    ViewCommentDTO findCommentById(Long commentId);

    void deleteComment(Long commentId);

    ViewCommentDTO updateComment(Long commentId, CommentDTO commentDTO);
}
