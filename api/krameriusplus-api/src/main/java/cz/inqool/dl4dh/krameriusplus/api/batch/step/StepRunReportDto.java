package cz.inqool.dl4dh.krameriusplus.api.batch.step;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class StepRunReportDto {

    private Set<PersistedErrorDto> errors = new HashSet<>();
}
