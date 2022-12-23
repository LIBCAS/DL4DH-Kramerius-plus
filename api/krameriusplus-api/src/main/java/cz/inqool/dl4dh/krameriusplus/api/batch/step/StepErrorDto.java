package cz.inqool.dl4dh.krameriusplus.api.batch.step;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepErrorDto {

    private String exitCode;

    private String shortMessage;

    private String stackTrace;
}
