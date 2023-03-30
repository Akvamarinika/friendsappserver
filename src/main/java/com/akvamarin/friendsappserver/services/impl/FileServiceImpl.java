package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.entity.location.City;
import com.akvamarin.friendsappserver.domain.entity.location.Country;
import com.akvamarin.friendsappserver.domain.entity.location.FederalDistrict;
import com.akvamarin.friendsappserver.domain.entity.location.Region;
import com.akvamarin.friendsappserver.repositories.location.CityRepository;
import com.akvamarin.friendsappserver.repositories.location.CountryRepository;
import com.akvamarin.friendsappserver.repositories.location.FederalDistrictRepository;
import com.akvamarin.friendsappserver.repositories.location.RegionRepository;
import com.akvamarin.friendsappserver.services.FileService;
import com.akvamarin.friendsappserver.utils.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor    //конструктор с 1 параметром для каждого поля
public class FileServiceImpl implements FileService {
    private final CountryRepository countryRepository;
    private final FederalDistrictRepository federalDistrictRepository;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;

    //@PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Override
    public void saveLocationExcelToDB(MultipartFile file) {
        if (ExcelHelper.isValidExcelFile(file)) {
            try (InputStream inputStream = file.getInputStream()) {
                List<City> cities = ExcelHelper.getCityDataFromExcel(inputStream);

                // save countries, regions, and federal districts, if they do not already exist
                for (City city : cities) {
                    Country country = city.getCountry();
                    Region region = city.getRegion();
                    FederalDistrict federalDistrict = region.getFederalDistrict();

                    federalDistrict = federalDistrictRepository.findByName(federalDistrict.getName())
                            .orElse(federalDistrictRepository.save(federalDistrict));

                    region.setFederalDistrict(federalDistrict);

                    country = countryRepository.findByName(country.getName())
                            .orElse(countryRepository.save(country));

                    region.setCountry(country);

                    region = regionRepository.findByNameAndCountry_Id(region.getName(), country.getId())
                            .orElse(regionRepository.save(region));

                    city.setCountry(country);
                    city.setRegion(region);
                    city.setFederalDistrict(federalDistrict);
                }

                cityRepository.saveAll(cities);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }


}
