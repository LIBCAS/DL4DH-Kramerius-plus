package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;


import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MergeExportsJobConfigDto extends ExportJobConfigDto{
    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_MERGE;
    }
}
