package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class EnrichJobConfigDto extends JobEventConfigCreateDto {

    @Schema(description = "If true and publications already exist, they will be overwritten. Defaults to false.")
    private boolean override = false;

    protected Map<String, Object> createJobParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("override", override);

        return parameters;
    }
}
