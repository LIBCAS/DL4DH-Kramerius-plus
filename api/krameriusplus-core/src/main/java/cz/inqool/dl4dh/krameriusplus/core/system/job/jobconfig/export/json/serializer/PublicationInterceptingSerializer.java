package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;

import java.io.IOException;

public class PublicationInterceptingSerializer extends StdSerializer<Publication> {

    private final SerializePart serializePart;

    public PublicationInterceptingSerializer(SerializePart serializePart) {
        super(Publication.class);
        this.serializePart = serializePart;
    }

    @Override
    public void serialize(Publication value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (serializePart == SerializePart.BEFORE_PAGES) {
            serializeBeforePart(value, gen, provider);
        } else {
            serializeAfterPart(value, gen, provider);
        }
    }

    @Override
    public void serializeWithType(Publication publication, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {
        serialize(publication, gen, provider);
    }

    private void serializeBeforePart(Publication value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("id", value.getId());
        gen.writeStringField("title", value.getTitle());
        gen.writeObjectField("modsMetadata", value.getModsMetadata());
        gen.writeObjectField("ocrParadata", value.getOcrParadata());
        gen.writeObjectField("udPipeParadata", value.getUdPipeParadata());
        gen.writeObjectField("nameTagParadata", value.getNameTagParadata());
        gen.writeRaw("\"pages\" : [");
    }

    private void serializeAfterPart(Publication value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeRaw(" ]");
        gen.writeString("\n}");
    }

    public enum SerializePart {
        BEFORE_PAGES,
        AFTER_PAGES
    }
}
