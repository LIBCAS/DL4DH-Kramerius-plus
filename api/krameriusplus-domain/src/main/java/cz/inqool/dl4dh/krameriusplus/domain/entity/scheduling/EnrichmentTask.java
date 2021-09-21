package cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document("tasks")
public class EnrichmentTask {

    @Id
    @JsonIgnore
    private String id = java.util.UUID.randomUUID().toString();

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

    public EnrichmentTask(String publicationId) {
        this.publicationId = publicationId;
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
        if (started == null || finished == null) {
            return null;
        }

        Duration duration = Duration.between(started, finished);

        return duration.toMillis() / (double) 1000 + "s";
    }

    public String getProcessing() {
        if (finished != null) {
            return null;
        }

        return processingPage + "/" + totalPages;
    }

    public String getDone() {
        if (state == State.SUCCESSFUL) {
            return "Done";
        }
        return percentDone + "%";
    }
}
