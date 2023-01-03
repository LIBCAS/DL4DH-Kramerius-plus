package cz.inqool.dl4dh.krameriusplus.corev2.job;

import com.fasterxml.jackson.databind.JavaType;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.JsonConverter;
import cz.inqool.dl4dh.krameriusplus.corev2.utils.JsonUtils;
import org.springframework.batch.core.JobParameters;

public class JobParametersJsonConverter extends JsonConverter<JobParameters> {

    @Override
    public JavaType getType() {
        return JsonUtils.typeFactory().constructType(JobParameters.class);
    }
}
