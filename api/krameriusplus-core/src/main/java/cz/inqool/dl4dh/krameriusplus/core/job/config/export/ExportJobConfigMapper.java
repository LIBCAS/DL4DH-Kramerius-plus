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

        throw new IllegalStateException("Mapping method not found for type " + dto.getClass().getSimpleName());
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

        throw new IllegalStateException("Mapping method not found for type " + entity.getClass().getSimpleName());
    }

    default ExportJobConfigDto toDto(ExportJobConfigView entity) {
        if (entity instanceof ExportAltoJobConfigView) {
            return toDto((ExportAltoJobConfigView) entity);
        } else if (entity instanceof ExportCsvJobConfigView) {
            return toDto((ExportCsvJobConfigView) entity);
        } else if (entity instanceof ExportJsonJobConfigView) {
            return toDto((ExportJsonJobConfigView) entity);
        } else if (entity instanceof ExportTeiJobConfigView) {
            return toDto((ExportTeiJobConfigView) entity);
        } else if (entity instanceof ExportTextJobConfigView) {
            return toDto((ExportTextJobConfigView) entity);
        }

        throw new IllegalStateException("Mapping method not found for type " + entity.getClass().getSimpleName());
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

    ExportTextJobConfigDto toDto(ExportTextJobConfigView entity);

    ExportTeiJobConfigDto toDto(ExportTeiJobConfigView entity);

    ExportJsonJobConfigDto toDto(ExportJsonJobConfigView entity);

    ExportAltoJobConfigDto toDto(ExportAltoJobConfigView entity);

    ExportCsvJobConfigDto toDto(ExportCsvJobConfigView entity);
}
