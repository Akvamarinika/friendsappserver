package com.example.authorization.enums.converters;

import com.example.authorization.enums.MsgStatus;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class MsgStatusConverter implements AttributeConverter<MsgStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MsgStatus msgStatus) {
        if (msgStatus == null){
            return 0;
        }

        return msgStatus.getNumberValue();
    }

    @JsonCreator    //конструктор или фабричный метод, используемый при десериализации (json => object)
    @Override
    public MsgStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null){
            return MsgStatus.UNKNOWN;
        }

        return Stream.of(MsgStatus.values())
                .filter(status -> status.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
