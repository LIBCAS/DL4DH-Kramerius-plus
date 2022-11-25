package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.executions.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.*;

/**
 * Custom entity representing an abstraction over spring's JobInstance fitted for Kramerius+
 */
@Getter
@Setter
@Entity
@ToString
public class JobEvent extends DatedObject {

    private String publicationId;

    @Column(unique = true)
    @NotNull
    private Long instanceId;

    @ManyToOne
    private JobEvent parent;

    @Embedded
    private ExecutionDetails details = new ExecutionDetails();

    @Embedded
    private JobEventConfig config;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "jobEvent")
    private Set<StepRunReport> stepRunReports = new HashSet<>();

    public boolean wasExecuted() {
        return details.getLastExecutionStatus() != JobStatus.CREATED;
    }

    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put(JOB_EVENT_ID, id);
        if (publicationId != null) {
            jobParametersMap.put(PUBLICATION_ID, publicationId);
        }
        jobParametersMap.put(KRAMERIUS_JOB, config.getKrameriusJob().name());
        jobParametersMap.putAll(config.getParameters());

        return jobParametersMap;
    }
}
