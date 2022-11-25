package cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto;

import cz.inqool.dl4dh.alto.Alto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageAndAltoDto extends DigitalObjectWithPathDto {

    private Alto alto;
}
