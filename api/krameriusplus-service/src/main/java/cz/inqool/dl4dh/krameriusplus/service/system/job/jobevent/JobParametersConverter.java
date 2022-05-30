package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.JsonConverter;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;

import javax.persistence.Converter;
import java.util.Map;

@Converter
public class JobParametersConverter extends JsonConverter<Map<String, Object>> {

    @Override
    public JavaType getType() {
        return JsonUtils.typeFactory().constructMapType(Map.class, String.class, Object.class);
    }
}
