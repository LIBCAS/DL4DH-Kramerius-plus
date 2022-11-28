package cz.inqool.dl4dh.krameriusplus.corev2.job.config;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.api.export.params.TeiParamsDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.JsonConverter;
import cz.inqool.dl4dh.krameriusplus.corev2.utils.JsonUtils;

public class TeiParamsJsonConverter extends JsonConverter<TeiParamsDto> {

    @Override
    public JavaType getType() {
        return JsonUtils.typeFactory().constructType(TeiParamsDto.class);
    }
}
