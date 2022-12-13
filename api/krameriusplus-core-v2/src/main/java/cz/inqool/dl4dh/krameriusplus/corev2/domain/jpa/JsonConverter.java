package cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

import static java.lang.String.format;


/**
 * Converter for database columns containing JSON-serialized objects.
 */
@Component
abstract public class JsonConverter<X> implements AttributeConverter<X, String> {

    protected ObjectMapper objectMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertToDatabaseColumn(X attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(format("Failed when serializing %s to JSON string",
                    attribute.getClass()), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public X convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            return objectMapper.readValue(dbData, getType());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(format("Failed to read JSON value as %s", getType().getTypeName()), e);
        }
    }

    /**
     * Return the type of serialized object.
     */
    public abstract JavaType getType();

    @Autowired
    public void setObjectMapper(@Qualifier("DB_JSON_CONVERTER") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
