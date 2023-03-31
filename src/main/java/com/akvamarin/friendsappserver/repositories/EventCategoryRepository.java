package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.event.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}

