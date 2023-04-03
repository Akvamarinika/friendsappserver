package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.dto.CityDTO;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.mapper.location.CityMapper;
import com.akvamarin.friendsappserver.repositories.location.CityRepository;
import com.akvamarin.friendsappserver.repositories.location.CountryRepository;
import com.akvamarin.friendsappserver.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CityMapper cityMapper;

    @Transactional
    @Override
    public City findCityIfNoCreateNew(@NotNull String nameCity, @NotNull String nameCountry) {
        City city = cityRepository.findByNameAndCountryName(nameCity, nameCountry).orElse(null);

        if (city != null) {
            return city;
        }

        Country country = countryRepository.findCountryByName(nameCountry)
                .orElseGet(() -> countryRepository.save(new Country(nameCountry)));

        City newCity = City.builder()
                .name(nameCity)
                .country(country)
                .build();

        return cityRepository.save(newCity);
    }

    @Transactional
    @Override
    public List<CityDTO> findAll() {
        List<CityDTO> result = cityRepository.findAll().stream()
                .map(cityMapper::toDTO)
                .collect(Collectors.toList());
        log.info("Method *** findAll *** : CityDTO list size = {}", result.size());
        return result;
    }

    @Transactional
    @Override
    public CityDTO findById(Long cityId) {
        CityDTO result = cityRepository.findById(cityId)
                .map(cityMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException(String.format("City with ID %d not found", cityId)));
        log.info("Method *** findById *** : CityDTO = {} cityId = {}", result, cityId);
        return result;
    }


}
