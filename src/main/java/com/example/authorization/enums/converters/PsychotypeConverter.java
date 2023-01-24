package com.example.authorization.enums.converters;

import com.example.authorization.enums.Psychotype;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsychotypeConverter implements AttributeConverter<Psychotype, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Psychotype psychotype) {
        if (psychotype == null){
            return 0;
        }

        return psychotype.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public Psychotype convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return Psychotype.NO_ANSWER;
        }

        return Stream.of(Psychotype.values())
                .filter(psychotype -> psychotype.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
