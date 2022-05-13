package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig;

import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.JobParametersConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("JpaAttributeTypeInspection") // bug in intellij causes warning on parameters attribute
@Getter
@Setter
@Embeddable
public class JobEventConfig {

    @Convert(converter = JobParametersConverter.class)
    private Map<String, Object> parameters = new HashMap<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private KrameriusJob krameriusJob;
}
