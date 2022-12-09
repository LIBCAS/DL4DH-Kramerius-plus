package cz.inqool.dl4dh.krameriusplus.corev2.job.config.enrichment;

import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentExternalJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentNdkJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.config.EnrichmentTeiJobConfigDto;
import org.mapstruct.Mapper;

@Mapper
public interface EnrichmentJobConfigMapper {

    default EnrichmentJobConfig fromDto(EnrichmentJobConfigDto dto) {
        if (dto instanceof EnrichmentExternalJobConfigDto) {
            return fromDto((EnrichmentExternalJobConfigDto) dto);
        } else if (dto instanceof EnrichmentNdkJobConfigDto) {
            return fromDto((EnrichmentNdkJobConfigDto) dto);
        } else if (dto instanceof EnrichmentTeiJobConfigDto) {
            return fromDto((EnrichmentTeiJobConfigDto) dto);
        }

        throw new IllegalStateException("Mapping method not found.");
    }

    default EnrichmentJobConfigDto toDto(EnrichmentJobConfig entity) {
        if (entity instanceof EnrichmentExternalJobConfig) {
            return toDto((EnrichmentExternalJobConfig) entity);
        } else if (entity instanceof EnrichmentNdkJobConfig) {
            return toDto((EnrichmentNdkJobConfig) entity);
        } else if (entity instanceof EnrichmentTeiJobConfig) {
            return toDto((EnrichmentTeiJobConfig) entity);
        }

        throw new IllegalStateException("Mapping method not found.");
    }

    EnrichmentExternalJobConfig fromDto(EnrichmentExternalJobConfigDto dto);

    EnrichmentNdkJobConfig fromDto(EnrichmentNdkJobConfigDto dto);

    EnrichmentTeiJobConfig fromDto(EnrichmentTeiJobConfigDto dto);

    EnrichmentExternalJobConfigDto toDto(EnrichmentExternalJobConfig entity);

    EnrichmentNdkJobConfigDto toDto(EnrichmentNdkJobConfig entity);

    EnrichmentTeiJobConfigDto toDto(EnrichmentTeiJobConfig entity);
}
