package com.akvamarin.friendsappserver.services;

import com.akvamarin.friendsappserver.domain.dto.CityDTO;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CityService {
    City findCityIfNoCreateNew(@NotNull String nameCity, @NotNull String nameCountry);

    @Transactional
    List<CityDTO> findAll();

    @Transactional
    CityDTO findById(Long cityId);
}
