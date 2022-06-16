package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AltoExportJobConfigDto extends ExportJobConfigDto {

    private Params params = new Params();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_ALTO;
    }

    @Override
    public Map<String, Object> getJobParameters() {
        return createJobParameters();
    }
}
