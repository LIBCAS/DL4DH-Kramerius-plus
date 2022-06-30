package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class JobEventConfigCreateDto {

    @Schema(hidden = true)
    protected Map<String, Object> jobParameters;

    @Schema(hidden = true)
    public abstract KrameriusJob getKrameriusJob();

    protected abstract void populateJobParameters();

    public Map<String, Object> getJobParameters() {
        jobParameters = new HashMap<>();
        populateJobParameters();

        return jobParameters;
    }
}
