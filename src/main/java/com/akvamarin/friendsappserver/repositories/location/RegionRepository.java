package com.akvamarin.friendsappserver.repositories.location;

import com.akvamarin.friendsappserver.domain.entity.location.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region,Long> {
    Optional<Region> findRegionByName(String name);
}
