package com.akvamarin.friendsappserver.repositories.location;

import com.akvamarin.friendsappserver.domain.entity.location.FederalDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FederalDistrictRepository extends JpaRepository<FederalDistrict,Long> {
    Optional<FederalDistrict> findFederalDistrictByName(String name);
}
