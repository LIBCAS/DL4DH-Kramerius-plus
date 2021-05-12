package cz.inqool.dl4dh.krameriusplus.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document("enrichment_task")
public class EnrichmentTask {

    @Id
    @JsonIgnore
    private String id = java.util.UUID.randomUUID().toString();

    private String rootPublicationId;

    private Instant created;

    private Instant finished;

    private long took;

    @JsonIgnore
    private int processingPage;

    @JsonIgnore
    private int totalPages;

    private State state;

    private String errorMessage;

    @Transient
    @JsonIgnore
    public double percentDone;

    public EnrichmentTask(String rootPublicationId) {
        this.rootPublicationId = rootPublicationId;
        created = Instant.now();
        state = State.CREATED;
    }

    public enum State {
        SUCCESSFUL,
        FAILED,
        CREATED,
        DOWNLOADING_PAGES,
        ENRICHING,
    }

    public String getTook() {
        return took == 0 ? null : (took / (double) 1000) + "s" ;
    }

    public String getProcessing() {
        return processingPage + "/" + totalPages;
    }

    public String getDone() {
        if (state == State.SUCCESSFUL) {
            return "Done";
        }
        return percentDone + "%";
    }
}
