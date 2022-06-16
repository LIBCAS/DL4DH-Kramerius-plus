package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class JobEventConfigCreateDto {

    @Schema(hidden = true)
    public abstract KrameriusJob getKrameriusJob();

    @Schema(hidden = true)
    public abstract Map<String, Object> getJobParameters();
}
