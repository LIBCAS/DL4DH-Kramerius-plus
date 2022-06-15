package cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.utils.JsonUtils;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.JobEventConfigCreateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "krameriusJob", include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CsvExportJobConfigDto.class, name = "EXPORT_CSV"),
        @JsonSubTypes.Type(value = JsonExportJobConfigDto.class, name = "EXPORT_JSON"),
        @JsonSubTypes.Type(value = TeiExportJobConfigDto.class, name = "EXPORT_TEI")
})
public abstract class ExportJobConfigDto extends JobEventConfigCreateDto {

    public abstract Params getParams();

    protected Map<String, Object> createJobParameters() {
        Map<String, Object> jobParameters = new HashMap<>();
        jobParameters.put("params", JsonUtils.toJsonString(getParams()));

        return jobParameters;
    }
}
