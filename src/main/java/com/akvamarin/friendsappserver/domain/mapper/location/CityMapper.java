package com.akvamarin.friendsappserver.domain.mapper.location;

import com.akvamarin.friendsappserver.domain.dto.CityDTO;
import com.akvamarin.friendsappserver.domain.dto.UserDTO;
import com.akvamarin.friendsappserver.domain.entity.User;
import com.akvamarin.friendsappserver.domain.entity.location.City;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "federalDistrictID", source = "region.federalDistrict.id")
    @Mapping(target = "federalDistrictName", source = "region.federalDistrict.name")
    @Mapping(target = "regionID", source = "region.id")
    @Mapping(target = "regionName", source = "region.name")
    @Mapping(target = "countryID", source = "region.federalDistrict.country.id")
    @Mapping(target = "countryName", source = "region.federalDistrict.country.name")
    CityDTO toDTO(City city);

    @BeanMapping(nullValueCheckStrategy = ALWAYS, nullValuePropertyMappingStrategy = IGNORE)
    @InheritInverseConfiguration
    @Mapping(target = "id", source = "id", ignore = true)
    City toEntity(CityDTO cityDTO);
}


