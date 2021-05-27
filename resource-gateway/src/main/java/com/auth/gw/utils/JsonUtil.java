package com.auth.gw.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

@Slf4j
public final class JsonUtil {
    private static ObjectMapper jsonToObjMapper = new ObjectMapper();
    private static ObjectMapper mapper = new ObjectMapper();

    static{
        jsonToObjMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
        jsonToObjMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        jsonToObjMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, BigDecimalToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long.class, ToStringSerializer.instance);

        jsonToObjMapper.registerModule(simpleModule);
    }


    private JsonUtil() {
        //do nothing
    }

    public static <T> T jsonToObject(String jsonInString, Class<T> clz) {
        try {
            return jsonToObjMapper.readValue(jsonInString, clz);
        } catch (Exception e) {
            log.error("{} {} error", jsonInString, clz.getName(), e);
        }
        return null;
    }

    public static String objectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("[json] to json error ", e);
            return null;
        }
    }


    @JacksonStdImpl
    public static class BigDecimalToStringSerializer extends ToStringSerializer {
        public static final BigDecimalToStringSerializer instance = new BigDecimalToStringSerializer();

        public BigDecimalToStringSerializer() {
            super(Object.class);
        }

        public BigDecimalToStringSerializer(Class<?> handledType) {
            super(handledType);
        }

        @Override
        public boolean isEmpty(SerializerProvider prov, Object value) {
            if (value == null) {
                return true;
            }
            String str = ((BigDecimal) value).stripTrailingZeros().toPlainString();
            return str.isEmpty();
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            gen.writeString(((BigDecimal) value).stripTrailingZeros().toPlainString());
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            return createSchemaNode("string", true);
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen,
                                      SerializerProvider provider, TypeSerializer typeSer)
                throws IOException {
            // no type info, just regular serialization
            serialize(value, gen, provider);
        }
    }

}
