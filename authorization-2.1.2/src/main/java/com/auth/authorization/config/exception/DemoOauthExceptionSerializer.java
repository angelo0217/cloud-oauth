package com.auth.authorization.config.exception;

import com.auth.authorization.model.AuthCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class DemoOauthExceptionSerializer extends StdSerializer<DemoOauthException> {
    public DemoOauthExceptionSerializer() {
        super(DemoOauthException.class);
    }

    @Override
    public void serialize(DemoOauthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("code", AuthCode.AUTH_ERROR.getCode());
        gen.writeStringField("message", AuthCode.AUTH_ERROR.getMessage());

        if (value.getAdditionalInformation()!=null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeStringField(key, add);
            }
        }

        gen.writeEndObject();
    }
}