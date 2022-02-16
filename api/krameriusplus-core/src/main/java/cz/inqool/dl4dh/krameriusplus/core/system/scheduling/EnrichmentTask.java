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
        private int totalPages = 0;
        private int currentPage = 0;

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

        public double getPercentDone() {
            double pagesDone = calculatePagesDone();

            if (subtasks.isEmpty()) {
                return pagesDone;
            } else {
                double oneTaskWeight = totalPages == 0 ? 1.0 / subtasks.size() : 1.0 / (subtasks.size() + 1);
                double done = totalPages == 0 ? 0 : pagesDone * oneTaskWeight;

                for (var subtask : subtasks) {
                    done += (subtask.getPercentDone() * oneTaskWeight);
                }

                return Math.round(done * 100) / 100.0;
            }
        }

        private double calculatePagesDone() {
            if (totalPages == 0) {
                return 0;
            }

            if (currentPage == totalPages) {
                return 1;
            }

            return Math.round((currentPage / (double) totalPages) * 100000) / (double) 100000;
        }

        public String getDone() {
            return (getPercentDone() * 100) + "%";
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

    @JsonIgnore
    public double getPercentDone() {
        return subtask.getPercentDone();
    }

    public void setErrorMessage(String message) {
        subtask.setErrorMessage(message);
    }

    public void setState(EnrichmentState state) {
        subtask.setState(state);
    }
}
