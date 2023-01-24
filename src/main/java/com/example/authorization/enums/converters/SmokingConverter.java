package com.example.authorization.enums.converters;

import com.example.authorization.enums.Smoking;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class SmokingConverter implements AttributeConverter<Smoking, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Smoking smoking) {
        if (smoking == null){
            return 0;
        }

        return smoking.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public Smoking convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return Smoking.NO_ANSWER;
        }

        return Stream.of(Smoking.values())
                .filter(smoking -> smoking.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
