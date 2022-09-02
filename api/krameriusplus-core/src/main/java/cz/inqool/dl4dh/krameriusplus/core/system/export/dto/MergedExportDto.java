package cz.inqool.dl4dh.krameriusplus.core.system.export.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MergedExportDto {

    @NotNull
    private FileRef fileRef;

    @NotNull
    private JobEventDto jobEventDto;
}
