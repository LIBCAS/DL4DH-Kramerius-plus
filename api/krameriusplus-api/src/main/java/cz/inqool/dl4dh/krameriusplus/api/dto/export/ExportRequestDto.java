package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.service.system.job.jobevent.jobeventconfig.dto.export.ExportJobConfigDto;

public interface ExportRequestDto {

    String getPublicationId();

    ExportJobConfigDto getConfig();
}
