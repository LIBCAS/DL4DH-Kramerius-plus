package cz.inqool.dl4dh.krameriusplus.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Norbert Bodnar
 */
@Getter
public class ScheduleMultipleDto {

    @NotNull
    private Set<String> publications;
}
