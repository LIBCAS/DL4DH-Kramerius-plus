package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.LaunchStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

import static cz.inqool.dl4dh.krameriusplus.api.batch.LaunchStatus.SUCCESS;

@Embeddable
@Getter
@Setter
public class LastLaunch {

    private Instant timestamp = Instant.now();

    @Enumerated(EnumType.STRING)
    private LaunchStatus launchStatus = SUCCESS;

    private String message;
}
