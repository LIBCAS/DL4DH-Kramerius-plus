package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.AltoParam;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.NameTagParam;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.TeiExportParams;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.tei.UdPipeParam;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;

@Getter
@Setter
public class TeiExportJobConfigDto extends ExportJobConfigDto {

    private TeiExportParams teiExportParams = new TeiExportParams();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_TEI;
    }

    @Override
    protected void populateJobParameters() {
        super.populateJobParameters();
        jobParameters.put(PARAMS, JsonUtils.toJsonString(teiExportParams));
    }

    @JsonProperty("params")
    public void unpackParams(Map<String, List<String>> params) {
        teiExportParams.setAltoParams(params.get("altoParams").stream().map(AltoParam::valueOf).collect(Collectors.toList()));
        teiExportParams.setUdPipeParams(params.get("udPipeParams").stream().map(UdPipeParam::valueOf).collect(Collectors.toList()));
        teiExportParams.setNameTagParams(params.get("nameTagParams").stream().map(NameTagParam::valueOf).collect(Collectors.toList()));
    }
}
