package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository  extends JpaRepository<Event,Long> {
}
