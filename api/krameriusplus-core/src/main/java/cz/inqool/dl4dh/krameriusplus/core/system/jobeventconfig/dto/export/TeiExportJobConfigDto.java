package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.TeiParams;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeiExportJobConfigDto extends ExportJobConfigDto {

    private TeiParams params = new TeiParams();

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_TEI;
    }
}
