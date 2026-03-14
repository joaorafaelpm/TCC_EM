package com.pendezzapizza.pendezzapizza_api.core.jackson;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Page;

import java.io.IOException;

@JsonComponent
public class PageJsonSerializer extends JsonSerializer<Page<?>> {

    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        writePageFields(page, gen);
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(Page<?> page, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        WritableTypeId typeId = typeSer.typeId(page, JsonToken.START_OBJECT);
        typeSer.writeTypePrefix(gen, typeId);
        writePageFields(page, gen);
        typeSer.writeTypeSuffix(gen, typeId);
    }

    private void writePageFields(Page<?> page, JsonGenerator gen) throws IOException {
        gen.writeObjectField("content", page.getContent());
        gen.writeNumberField("size", page.getSize());
        gen.writeNumberField("totalElements", page.getTotalElements());
        gen.writeNumberField("totalPage", page.getTotalPages());
        gen.writeNumberField("currentPage", page.getNumber());
    }
}