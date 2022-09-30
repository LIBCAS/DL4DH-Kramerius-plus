package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigDto;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;

@Getter
@Setter
public abstract class ExportJobConfigDto extends JobEventConfigDto {

    private Params params = new Params();

    public abstract ExportFormat getFormat();

    @Override
    public Map<String, Object> toJobParametersMap() {
        Map<String, Object> jobParametersMap = new HashMap<>();
        jobParametersMap.put(PARAMS, JsonUtils.toJsonString(params));

        return jobParametersMap;
    }
}
