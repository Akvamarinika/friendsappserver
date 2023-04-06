package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
