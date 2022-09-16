package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;


import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class MergeExportsJobConfigDto extends JobEventConfigDto {

    @Override
    public KrameriusJob getKrameriusJob() {
        return KrameriusJob.EXPORT_MERGE;
    }

    @Override
    public Map<String, Object> toJobParametersMap() {
        return new HashMap<>();
    }
}
