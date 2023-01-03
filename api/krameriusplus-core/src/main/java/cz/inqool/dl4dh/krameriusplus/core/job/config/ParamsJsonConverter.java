package cz.inqool.dl4dh.krameriusplus.core.job.config;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.api.export.params.ParamsDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.JsonConverter;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;

public class ParamsJsonConverter extends JsonConverter<ParamsDto> {

    @Override
    public JavaType getType() {
        return JsonUtils.typeFactory().constructType(ParamsDto.class);
    }
}
