package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.event.Event;
import com.akvamarin.friendsappserver.domain.entity.message.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(Long eventId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByEventOrderByCreatedAtDesc(Event event);

    List<Comment> findAllByEventOrderByCreatedAtDesc(Event event);

    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
}
