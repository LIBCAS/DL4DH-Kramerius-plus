package cz.inqool.dl4dh.krameriusplus.api.batch;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class LastLaunchDto {

    private Instant timestamp;

    private LaunchStatus launchStatus;

    private String message;
}
