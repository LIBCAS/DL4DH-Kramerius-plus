package cz.inqool.dl4dh.krameriusplus.core.system.scheduling;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentState.CREATED;
import static cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentState.SUCCESSFUL;

/**
 * @author Norbert Bodnar
 */
@Getter
@Setter
@Document("tasks")
@AllArgsConstructor
@NoArgsConstructor
public class EnrichmentTask extends DomainObject {

    @Field("subtask")
    private EnrichmentSubTask subtask;

    @Transient
    @JsonIgnore
    private Future<String> future;

    public EnrichmentTask(String publicationId) {
        this.subtask = new EnrichmentSubTask(publicationId);
    }

    @Getter
    @Setter
    public static class EnrichmentSubTask {

        private String publicationId;
        private String publicationTitle;
        private Instant created;
        private Instant started;
        private Instant finished;
        private EnrichmentState state;
        private String errorMessage;
        private List<EnrichmentSubTask> subtasks = new ArrayList<>();
        @JsonIgnore
        private int totalPages = 0;
        @JsonIgnore
        private int currentPage = 0;
        @Transient
        @JsonIgnore
        public double percentDone;

        public EnrichmentSubTask(String publicationId) {
            this.publicationId = publicationId;
            created = Instant.now();
            state = CREATED;
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

            return currentPage + "/" + totalPages;
        }

        public String getDone() {
            if (percentDone == 0.0) {
                return null;
            }
            if (state == SUCCESSFUL) {
                return "Done";
            }
            return percentDone + "%";
        }
    }

    public String getTook() {
        return subtask.getTook();
    }

    public String getProcessing() {
        return subtask.getProcessing();
    }

    public String getDone() {
        return subtask.getDone();
    }

    public void setErrorMessage(String message) {
        subtask.setErrorMessage(message);
    }

    public void setState(EnrichmentState state) {
        subtask.setState(state);
    }
}
