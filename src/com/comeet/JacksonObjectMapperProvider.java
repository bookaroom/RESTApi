package com.comeet;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(final Class<?> type) {
        final ObjectMapper toReturn = new ObjectMapper();
        toReturn.enable(SerializationFeature.INDENT_OUTPUT);
        toReturn.disable(MapperFeature.USE_ANNOTATIONS);
        return toReturn;
    }
}
