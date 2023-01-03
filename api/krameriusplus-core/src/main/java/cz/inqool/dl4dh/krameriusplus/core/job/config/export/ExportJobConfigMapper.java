package cz.inqool.dl4dh.krameriusplus.core.job.config.export;

import cz.inqool.dl4dh.krameriusplus.api.batch.config.*;
import org.mapstruct.Mapper;

@Mapper
public interface ExportJobConfigMapper {

    default ExportJobConfig fromDto(ExportJobConfigDto dto) {
        if (dto instanceof ExportAltoJobConfigDto) {
            return fromDto((ExportAltoJobConfigDto) dto);
        } else if (dto instanceof ExportCsvJobConfigDto) {
            return fromDto((ExportCsvJobConfigDto) dto);
        } else if (dto instanceof ExportJsonJobConfigDto) {
            return fromDto((ExportJsonJobConfigDto) dto);
        } else if (dto instanceof ExportTeiJobConfigDto) {
            return fromDto((ExportTeiJobConfigDto) dto);
        } else if (dto instanceof ExportTextJobConfigDto) {
            return fromDto((ExportTextJobConfigDto) dto);
        }

        throw new IllegalStateException("Mapping method not found.");
    }

    default ExportJobConfigDto toDto(ExportJobConfig entity) {
        if (entity instanceof ExportAltoJobConfig) {
            return toDto((ExportAltoJobConfig) entity);
        } else if (entity instanceof ExportCsvJobConfig) {
            return toDto((ExportCsvJobConfig) entity);
        } else if (entity instanceof ExportJsonJobConfig) {
            return toDto((ExportJsonJobConfig) entity);
        } else if (entity instanceof ExportTeiJobConfig) {
            return toDto((ExportTeiJobConfig) entity);
        } else if (entity instanceof ExportTextJobConfig) {
            return toDto((ExportTextJobConfig) entity);
        }

        throw new IllegalStateException("Mapping method not found.");
    }

    ExportAltoJobConfig fromDto(ExportAltoJobConfigDto dto);

    ExportCsvJobConfig fromDto(ExportCsvJobConfigDto dto);

    ExportJsonJobConfig fromDto(ExportJsonJobConfigDto dto);

    ExportTeiJobConfig fromDto(ExportTeiJobConfigDto dto);

    ExportTextJobConfig fromDto(ExportTextJobConfigDto dto);

    ExportAltoJobConfigDto toDto(ExportAltoJobConfig entity);

    ExportCsvJobConfigDto toDto(ExportCsvJobConfig entity);

    ExportJsonJobConfigDto toDto(ExportJsonJobConfig entity);

    ExportTeiJobConfigDto toDto(ExportTeiJobConfig entity);

    ExportTextJobConfigDto toDto(ExportTextJobConfig entity);
}
