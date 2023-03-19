package com.akvamarin.friendsappserver.repositories;

import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.entity.location.FederalDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {
    Optional<Country> findCountryByName(String name);
}
