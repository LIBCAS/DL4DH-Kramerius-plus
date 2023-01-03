package cz.inqool.dl4dh.krameriusplus.core.job;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.JsonConverter;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import org.springframework.batch.core.JobParameters;

public class JobParametersJsonConverter extends JsonConverter<JobParameters> {

    @Override
    public JavaType getType() {
        return JsonUtils.typeFactory().constructType(JobParameters.class);
    }
}
