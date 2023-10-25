package net.mlk.adolfserver.data.converters;

import jakarta.persistence.AttributeConverter;
import net.mlk.jmson.JsonList;

public class ListConverter implements AttributeConverter<JsonList, String> {
    @Override
    public String convertToDatabaseColumn(JsonList attribute) {
        return attribute.toString();
    }

    @Override
    public JsonList convertToEntityAttribute(String dbData) {
        return new JsonList(dbData);
    }
}
