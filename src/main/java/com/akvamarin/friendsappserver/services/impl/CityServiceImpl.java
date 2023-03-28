package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.mapper.CityMapper;
import com.akvamarin.friendsappserver.repositories.location.CityRepository;
import com.akvamarin.friendsappserver.repositories.location.CountryRepository;
import com.akvamarin.friendsappserver.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

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


}
