package cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.dto.DatedObjectDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobStatus;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.LastExecutionDetails;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobEventConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobEventDto extends DatedObjectDto {

    private String jobName;

    private String publicationId;

    @JsonIgnore
    private Long instanceId;
    @JsonUnwrapped
    private LastExecutionDetails details;

    private JobEventDto parent;

    private JobEventConfig config;
}
