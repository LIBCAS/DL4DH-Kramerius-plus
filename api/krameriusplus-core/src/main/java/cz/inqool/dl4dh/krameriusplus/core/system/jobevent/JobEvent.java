package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DatedObject;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJob;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.BatchStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("JpaAttributeTypeInspection") // bug in intellij causes warning on parameters attribute
@Getter
@Setter
@Entity
@ToString
public class JobEvent extends DatedObject {

    private String jobName;

    private String publicationId;

    @Column(unique = true)
    private Long instanceId;

    private Long lastExecutionId;

    @Enumerated(EnumType.STRING)
    private BatchStatus lastExecutionStatus;

    @ManyToOne
    private JobEvent parent;

    @Convert(converter = JobParametersConverter.class)
    private Map<String, Object> parameters = new HashMap<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusJob krameriusJob;
}
