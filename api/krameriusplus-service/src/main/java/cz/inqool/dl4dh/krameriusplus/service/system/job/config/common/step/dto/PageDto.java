package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto extends ProcessingDto {

    private Page page;
}
