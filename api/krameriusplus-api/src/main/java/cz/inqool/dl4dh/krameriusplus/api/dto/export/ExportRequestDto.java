package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.ExportJobConfigDto;

public interface ExportRequestDto {

    String getName();

    String getPublicationId();

    ExportJobConfigDto getConfig();
}
