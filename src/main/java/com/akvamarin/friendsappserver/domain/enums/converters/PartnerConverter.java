package com.akvamarin.friendsappserver.domain.enums.converters;

import com.akvamarin.friendsappserver.domain.enums.Partner;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PartnerConverter implements AttributeConverter<Partner, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Partner partner) {
        if (partner == null){
            return 0;
        }

        return partner.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public Partner convertToEntityAttribute(Integer dbData) {
        if (dbData == null || dbData == 0){
            return Partner.ANY;
        }

        return Stream.of(Partner.values())
                .filter(partner -> partner.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
