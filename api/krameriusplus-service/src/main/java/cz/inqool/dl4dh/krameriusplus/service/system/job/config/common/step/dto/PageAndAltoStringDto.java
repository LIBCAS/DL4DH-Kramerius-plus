package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageAndAltoStringDto {
    private final Page page;
    private final String altoString;
}
