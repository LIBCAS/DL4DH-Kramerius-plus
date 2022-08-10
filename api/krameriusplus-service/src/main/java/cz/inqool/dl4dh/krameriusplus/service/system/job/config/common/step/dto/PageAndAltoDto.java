package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageAndAltoDto {
    private final Page page;
    private final Alto alto;
}
