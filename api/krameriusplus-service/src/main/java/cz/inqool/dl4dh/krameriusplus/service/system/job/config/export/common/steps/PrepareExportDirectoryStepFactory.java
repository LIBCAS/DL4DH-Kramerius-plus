package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.PrepareExportDirectoryTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.PREPARE_EXPORT_DIRECTORY;

@Component
public class PrepareExportDirectoryStepFactory extends AbstractStepFactory {

    private final PrepareExportDirectoryTasklet tasklet;

    @Autowired
    public PrepareExportDirectoryStepFactory(PrepareExportDirectoryTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(PREPARE_EXPORT_DIRECTORY)
                .tasklet(tasklet)
                .build();
    }
}
