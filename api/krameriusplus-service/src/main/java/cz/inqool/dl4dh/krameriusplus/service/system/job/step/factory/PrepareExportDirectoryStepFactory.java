package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.PrepareExportDirectoryTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.PREPARE_EXPORT_DIRECTORY;

@Component
public class PrepareExportDirectoryStepFactory extends AbstractStepFactory {

    private final PrepareExportDirectoryTasklet tasklet;

    @Autowired
    public PrepareExportDirectoryStepFactory(PrepareExportDirectoryTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(PREPARE_EXPORT_DIRECTORY)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return PREPARE_EXPORT_DIRECTORY;
    }
}
