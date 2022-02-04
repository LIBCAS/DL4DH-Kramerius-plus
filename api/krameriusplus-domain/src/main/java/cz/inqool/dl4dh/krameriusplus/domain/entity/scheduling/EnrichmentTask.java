package cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.domain.entity.DomainObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Future;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document("tasks")
public class EnrichmentTask extends DomainObject {

    private String publicationId;
    private String publicationTitle;
    private Instant created;
    private Instant started;
    private Instant finished;

    @JsonIgnore
    private int processingPage;

    @JsonIgnore
    private int totalPages;

    private State state;

    private String errorMessage;

    @Transient
    @JsonIgnore
    public double percentDone;

    @Transient
    @JsonIgnore
    private Future<String> future;

    public EnrichmentTask(String publicationId) {
        this.publicationId = publicationId;
        created = Instant.now();
        state = State.CREATED;
    }

    public enum State {
        SUCCESSFUL,
        FAILED,
        CREATED,
        DOWNLOADING,
        ENRICHING,
        CANCELED
    }

    public String getTook() {
        if (started == null || finished == null) {
            return null;
        }

        Duration duration = Duration.between(started, finished);

        return duration.toMillis() / (double) 1000 + "s";
    }

    public String getProcessing() {
        if (finished != null || totalPages == 0) {
            return null;
        }

        return processingPage + "/" + totalPages;
    }

    public String getDone() {
        if (percentDone == 0.0) {
            return null;
        }
        if (state == State.SUCCESSFUL) {
            return "Done";
        }
        return percentDone + "%";
    }
}
