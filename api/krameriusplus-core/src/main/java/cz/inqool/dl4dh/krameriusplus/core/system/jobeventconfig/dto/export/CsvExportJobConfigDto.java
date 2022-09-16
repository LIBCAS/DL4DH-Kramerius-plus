package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.DELIMITER;

@Getter
@Setter
public class CsvExportJobConfigDto extends ExportJobConfigDto {

    @Schema(description = "Delimiter used to generate export.", defaultValue = ",")
    private String delimiter = ",";

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_CSV;
    }

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = super.toJobParametersMap();
        jobParametersMap.put(DELIMITER, delimiter);

        return jobParametersMap;
    }
}
