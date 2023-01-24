package com.example.authorization.enums.converters;



import com.example.authorization.enums.AddStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AddStatusConverter implements AttributeConverter<AddStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AddStatus addStatus) {
        if (addStatus == null){
            return 0;
        }

        return addStatus.getNumberValue();
    }

    @Override
    public AddStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == 0){
            return AddStatus.UNKNOWN;
        }

        return Stream.of(AddStatus.values())
                .filter(addStatus -> addStatus.getNumberValue() == dbData)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
