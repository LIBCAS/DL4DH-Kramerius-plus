package cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.JobEventConfigCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PARAMS;

@Getter
@Setter
public abstract class ExportJobConfigDto extends JobEventConfigCreateDto {

    public abstract Params getParams();

    @Override
    protected void populateJobParameters() {
        jobParameters.put(PARAMS, JsonUtils.toJsonString(getParams()));
    }
}
