package cz.inqool.dl4dh.krameriusplus.core.system.job.jobevent.jobeventconfig.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.KrameriusJob;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "krameriusJob")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DownloadKStructureJobConfigDto.class, name = "DOWNLOAD_K_STRUCTURE"),
        @JsonSubTypes.Type(value = EnrichExternalJobConfigDto.class, name = "ENRICH_EXTERNAL"),
        @JsonSubTypes.Type(value = EnrichNdkJobConfigDto.class, name = "ENRICH_NDK"),
        @JsonSubTypes.Type(value = EnrichTeiJobConfigDto.class, name = "ENRICH_TEI"),
        @JsonSubTypes.Type(value = ExportingJobConfigDto.class, name = "EXPORTING_JOB"),
})
public abstract class JobEventConfigCreateDto {

    public abstract KrameriusJob getKrameriusJob();

    public abstract Map<String, Object> getJobParameters();
}
