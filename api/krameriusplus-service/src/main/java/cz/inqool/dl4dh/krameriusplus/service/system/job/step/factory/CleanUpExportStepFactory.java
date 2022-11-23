package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.CleanUpExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.CLEAN_UP_EXPORT;

@Component
public class CleanUpExportStepFactory extends AbstractStepFactory {

    private final CleanUpExportTasklet tasklet;

    @Autowired
    public CleanUpExportStepFactory(CleanUpExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(CLEAN_UP_EXPORT)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CLEAN_UP_EXPORT;
    }
}
