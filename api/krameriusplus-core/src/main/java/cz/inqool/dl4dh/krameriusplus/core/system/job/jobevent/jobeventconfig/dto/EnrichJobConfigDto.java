package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class EnrichJobConfigDto extends JobEventConfigCreateDto {

    private boolean override = false;

    protected Map<String, Object> createJobParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("override", override);

        return parameters;
    }
}
