package com.example.authorization.enums.converters;

import com.example.authorization.enums.PeriodOfTime;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PeriodOfTimeConverter implements AttributeConverter<PeriodOfTime, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PeriodOfTime periodOfTime) {
        if (periodOfTime == null){
            return 0;
        }

        return periodOfTime.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public PeriodOfTime convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return PeriodOfTime.EVENING;
        }

        return Stream.of(PeriodOfTime.values())
                .filter(periodOfTime -> periodOfTime.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
