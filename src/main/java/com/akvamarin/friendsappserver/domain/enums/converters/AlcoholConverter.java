package com.akvamarin.friendsappserver.domain.enums.converters;

import com.akvamarin.friendsappserver.domain.enums.Alcohol;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AlcoholConverter implements AttributeConverter<Alcohol, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Alcohol alcohol) {
        if (alcohol == null){
            return 0;
        }

        return alcohol.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public Alcohol convertToEntityAttribute(Integer dbData) {
        if (dbData == null || dbData == 0 ){
            return Alcohol.NO_ANSWER;
        }

        return Stream.of(Alcohol.values())
                .filter(alcohol -> alcohol.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
