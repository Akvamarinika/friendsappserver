package com.akvamarin.friendsappserver.repositories.location;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City,Long> {
    Optional<City> findCityByName(String name);
    Optional<City> findByNameAndRegion_Id(String name, Long regionId);
    Optional<City> findByNameAndCountryName(String cityName, String countryName);
}
