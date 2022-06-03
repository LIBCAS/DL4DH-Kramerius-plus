package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import org.springframework.batch.core.Step;

public interface StepFactory {

    Step build();
}
