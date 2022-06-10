package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExportJobConfigDto extends JobEventConfigCreateDto {

    private String publicationTitle;

    private Params params;

    private ExportFormat exportFormat;

    private final KrameriusJob krameriusJob = KrameriusJob.BATCH_EXPORT;

    @Override
    public Map<String, Object> getJobParameters() {
        Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("publicationTitle", publicationTitle);
        jobParameters.put("params", JsonUtils.toJsonString(params));
        jobParameters.put("exportFormat", exportFormat);

        return jobParameters;
    }
}
