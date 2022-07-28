package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom entity representing an abstraction over spring's JobInstance fitted for Kramerius+
 */
@Getter
@Setter
@Entity
@ToString
public class JobEvent extends DatedObject {

    private String jobName;

    private String publicationId;

    @Column(unique = true)
    private Long instanceId;

    @ManyToOne
    private JobEvent parent;

    @Embedded
    private LastExecutionDetails details = new LastExecutionDetails();

    @Embedded
    private JobEventConfig config;

    public boolean wasExecuted() {
        return details != null && details.getLastExecutionId() != null;
    }

    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put("jobEventId", id);
        jobParametersMap.put("jobEventName", jobName);
        jobParametersMap.put("publicationId", publicationId);
        jobParametersMap.put("created", Date.from(created));
        jobParametersMap.put("krameriusJob", config.getKrameriusJob().name());
        jobParametersMap.putAll(config.getParameters());

        return jobParametersMap;
    }
}
