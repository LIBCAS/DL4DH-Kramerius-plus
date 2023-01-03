package cz.inqool.dl4dh.krameriusplus.api.publication.paradata;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.beans.Transient;
import java.time.Instant;

/**
 * Metadata about metadata
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "externalSystem", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = NameTagEnrichmentParadata.class, name = "NAME_TAG"),
        @JsonSubTypes.Type(value = UDPipeEnrichmentParadata.class, name = "UD_PIPE"),
        @JsonSubTypes.Type(value = OCREnrichmentParadata.class, name = "OCR"),
})
public abstract class EnrichmentParadata {

    protected Instant processingStarted;

    protected Instant processingFinished;

    @Transient
    public abstract ExternalSystem getExternalSystem();
}
