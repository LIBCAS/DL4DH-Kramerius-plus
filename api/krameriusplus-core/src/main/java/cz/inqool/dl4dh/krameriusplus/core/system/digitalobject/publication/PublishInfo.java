package cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication;

import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PublishInfo {

    /**
     * Flag for publishing publication. Default to false. Administrator can
     * set this flag for external systems, so they know that the enriched publication
     * is ready to be shared/indexed.
     */
    @Getter(AccessLevel.NONE)
    private boolean isPublished = false;

    /**
     * Instant of the last modified timestamp for published
     */
    private Instant publishedLastModified;

    public void publish() {
        isPublished = true;
        publishedLastModified = Instant.now();
    }

    public void unPublish() {
        isPublished = false;
        publishedLastModified = Instant.now();
    }
}
