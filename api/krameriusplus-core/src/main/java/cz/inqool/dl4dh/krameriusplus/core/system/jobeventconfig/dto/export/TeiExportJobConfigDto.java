package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.TEI_EXPORT_PARAMS;

@Getter
@Setter
public class TeiExportJobConfigDto extends ExportJobConfigDto {

    private TeiExportParams teiExportParams = new TeiExportParams();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_TEI;
    }

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = super.toJobParametersMap();
        jobParametersMap.put(TEI_EXPORT_PARAMS, JsonUtils.toJsonString(teiExportParams));

        return jobParametersMap;
    }
}
