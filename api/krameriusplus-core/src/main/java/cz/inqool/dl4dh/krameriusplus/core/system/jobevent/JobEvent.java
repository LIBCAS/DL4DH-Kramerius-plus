package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.batch.job.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("JpaAttributeTypeInspection") // bug in intellij causes warning on parameters attribute
@Getter
@Setter
@Entity
public class JobEvent extends DatedObject {

    private String jobName;

    private String publicationId;

    @Column(unique = true)
    private Long instanceId;

    private Long lastExecutionId;

    @ManyToOne
    private JobEvent parent;

    @Convert(converter = JobParametersConverter.class)
    private Map<String, Object> parameters = new HashMap<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusJob krameriusJob;

    public JobParameters toJobParameters() {
        JobParametersBuilder builder = new JobParametersBuilder()
                .addString("jobEventId", id)
                .addString("jobEventName", jobName)
                .addString("publicationId", publicationId)
                .addDate("created", Date.from(created));

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry.getValue() instanceof String) {
                builder.addString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Date) {
                builder.addDate(entry.getKey(), (Date) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                builder.addLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                builder.addDouble(entry.getKey(), (Double) entry.getValue());
            } else {
                builder.addString(entry.getKey(), JsonUtils.toJsonString(entry.getValue()));
            }
        }

        return builder.toJobParameters();
    }
}
