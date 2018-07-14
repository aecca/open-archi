package com.araguacaima.open_archi.persistence.commons;

import com.araguacaima.commons.utils.EnumsUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.lang.annotation.ElementType;

@Converter(autoApply = true)
public class ElementTypeConverter implements AttributeConverter<ElementType, String> {

    private static final EnumsUtils<ElementType> enumsUtils = new EnumsUtils<>();

    @Override
    public String convertToDatabaseColumn(ElementType elementType) {
        return elementType.name();
    }

    @Override
    public ElementType convertToEntityAttribute(String dbData) {
        return enumsUtils.getEnum(ElementType.class, dbData);
    }

}