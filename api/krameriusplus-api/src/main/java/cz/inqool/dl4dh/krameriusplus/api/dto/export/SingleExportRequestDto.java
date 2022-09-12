package cz.inqool.dl4dh.krameriusplus.api.dto.export;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.ExportJobConfigDto;

import java.util.Set;

public interface SingleExportRequestDto {

    String getName();

    Set<String> getPublications();

    ExportJobConfigDto getConfig();
}
