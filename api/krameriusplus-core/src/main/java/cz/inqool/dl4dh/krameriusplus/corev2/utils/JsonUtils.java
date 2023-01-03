package cz.inqool.dl4dh.krameriusplus.corev2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationContext;

import static java.lang.String.format;

/**
 * Static class containing JSON-oriented utility methods
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper objectMapper;

    private static void ensureObjectMapperInitialized() {
        if (objectMapper == null) {
            ApplicationContext applicationContext = ApplicationContextUtils.getApplicationContext();
            if (applicationContext != null) {
                objectMapper = applicationContext.getBean(ObjectMapper.class);
            } else {
                throw new RuntimeException("Application not properly initialized yet.");
            }
        }
    }

    public static String toJsonString(Object o) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException(format("Failed when serializing %s to JSON string", (o != null) ? o.getClass() : null), e);
        }
    }

    public static <T> T fromJsonString(@NonNull String json, @NonNull Class<T> type) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Failed to read JSON value as %s", type.getName()), e);
        }
    }

    public static <T> T fromJsonString(@NonNull String json, @NonNull TypeReference<T> type) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Failed to read JSON value as %s", type), e);
        }
    }

    public static <T> T fromJsonString(@NonNull String json, @NonNull JavaType type) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Failed to read JSON value as %s", type.getTypeName()), e);
        }
    }

    public static <T> T fromJsonStringParametrized(@NonNull String json, @NonNull Class<T> parametrized, Class<?>... parameterTypes) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructParametricType(parametrized, parameterTypes));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Failed to read JSON value as %s", parametrized.getName()), e);
        }
    }

    public static <IN, OUT> OUT convert(IN input, @NonNull Class<OUT> outputType) {
        ensureObjectMapperInitialized();

        try {
            return objectMapper.convertValue(input, outputType);
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to convert %s to %s", (input != null) ? input.getClass() : null, outputType), e);
        }
    }

    public static <IN, OUT> OUT convert(IN input, @NonNull Class<OUT> outputType, Class<?>... parameterTypes) {
        ensureObjectMapperInitialized();

        try {
            JavaType parametricType = objectMapper.getTypeFactory().constructParametricType(outputType, parameterTypes);
            return objectMapper.convertValue(input, parametricType);
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to convert %s to %s", (input != null) ? input.getClass() : null, outputType), e);
        }
    }

    public static TypeFactory typeFactory() {
        ensureObjectMapperInitialized();
        return objectMapper.getTypeFactory();
    }
}
