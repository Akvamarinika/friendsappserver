package com.example.authorization.enums.converters;

import com.example.authorization.enums.Sex;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SexConverter implements AttributeConverter<Sex, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Sex sex) {
        if (sex == null){
            return 0;
        }

        return sex.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public Sex convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return Sex.NO_ANSWER;
        }

        return Stream.of(Sex.values())
                .filter(sex -> sex.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
