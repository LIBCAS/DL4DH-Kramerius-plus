package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;

@Getter
@Setter
public abstract class ExportJobConfigDto extends JobEventConfigCreateDto {

    public abstract Params getParams();

    protected Map<String, Object> createJobParameters() {
        Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put(PARAMS, JsonUtils.toJsonString(getParams()));

        return jobParameters;
    }
}
