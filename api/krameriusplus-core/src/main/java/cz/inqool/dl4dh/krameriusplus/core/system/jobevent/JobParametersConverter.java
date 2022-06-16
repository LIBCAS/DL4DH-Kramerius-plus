package cz.inqool.dl4dh.krameriusplus.core.system.jobevent;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.JsonConverter;
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
