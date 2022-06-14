package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.DELIMITER;

@Getter
@Setter
public class CsvExportJobConfigDto extends ExportJobConfigDto {

    @Schema(description = "Delimiter used to generate export.", defaultValue = ",")
    private String delimiter = ",";

    private Params params;

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_CSV;
    }

    @Override
    public Map<String, Object> getJobParameters() {
        Map<String, Object> parameters = createJobParameters();
        parameters.put(DELIMITER, delimiter);

        return parameters;
    }
}
