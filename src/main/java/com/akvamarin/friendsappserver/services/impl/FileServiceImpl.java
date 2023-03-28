package com.akvamarin.friendsappserver.services.impl;

import com.akvamarin.friendsappserver.domain.entity.location.City;
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
    private final FederalDistrictRepository districtRepository;
    private final RegionRepository regionRepository;
    private final CityRepository cityRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Override
    public void saveLocationExcelToDB(MultipartFile file) {
        if(ExcelHelper.isValidExcelFile(file)){
            try (InputStream inputStream = file.getInputStream()){
                List<City> cities = ExcelHelper.getCityDataFromExcel(inputStream);
               // countryRepository.saveAll(ExcelHelper.countries);
                cityRepository.saveAll(cities);


               /* Country country = new Country("Cccc");
                Region region = new Region("Rrrrr");
                FederalDistrict federalDistrict = new FederalDistrict("Ffff");
                City city = City.builder()
                        .name("Ciyu")
                        .build();

                City city2 = City.builder()
                        .name("Ciyu222")
                        .build();

                country.addCityToCountry(city);
                region.addCityToRegion(city);
                federalDistrict.addRegionToFederalDistrict(region);

                country.addCityToCountry(city2);
                region.addCityToRegion(city2);

                countryRepository.save(country);
                districtRepository.save(federalDistrict);*/

              /*  citiesFromExcel.stream().map( city -> {
                    Country country = city.getCountry();
                    Region region = city.getRegion();
                    FederalDistrict federalDistrict = region.getFederalDistrict();
                    log.info(String.format("Country: %d", country.getId())); */

                  /*  country = countryRepository
                            .findCountryByName(country.getName())
                            .orElse(countryRepository.save(country));
                    log.info(String.format("Country with ID: %d", country.getId()));
                    System.out.println(country.toString());

                    federalDistrict = districtRepository
                            .findFederalDistrictByName(federalDistrict.getName())
                            .orElse(districtRepository.save(federalDistrict));

                    region.setFederalDistrict(federalDistrict);
                    region = regionRepository
                            .findRegionByName(region.getName())
                            .orElse(regionRepository.save(region));*/

                  /*  country.addCityToCountry(city);
                    federalDistrict.addRegionToFederalDistrict(region);
                    region.addCityToRegion(city);
                    countryRepository.save(country);
                    districtRepository.save(federalDistrict); */
                   //city.setCountry(country);
                   //city.setRegion(region);

                 /*   city = cityRepository
                            .findByNameAndRegion_Id(city.getName(), region.getId())
                            .orElse(cityRepository.save(city));*/

                /*    boolean isDistrictInDB = districtRepository
                            .findFederalDistrictByName(federalDistrict.getName())
                            .isPresent();
                    if (!isDistrictInDB) {federalDistrict = districtRepository.save(federalDistrict);}

                    boolean isRegionInDB = regionRepository
                            .findRegionByName(region.getName())
                            .isPresent();
                    if (!isDistrictInDB) {region = districtRepository.save(federalDistrict);}*/

                //    return city;
                //}).forEach(System.out::println);;

               // this.cityRepository.saveAllAndFlush(citiesFromExcel);
               // System.out.println(citiesFromExcel.get(5).getCountry().toString());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }


}
