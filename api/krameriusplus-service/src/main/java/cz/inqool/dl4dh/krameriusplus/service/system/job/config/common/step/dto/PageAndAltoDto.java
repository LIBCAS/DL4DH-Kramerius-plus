package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.dto;

import cz.inqool.dl4dh.alto.Alto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageAndAltoDto extends PageWithPathDto {

    private Alto alto;
}
