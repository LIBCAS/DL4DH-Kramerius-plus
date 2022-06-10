package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.batched.components.ZipExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.ZIP_EXPORT;

@Component
public class ZipExportStepFactory extends AbstractStepFactory {

    private final ZipExportTasklet tasklet;

    @Autowired
    public ZipExportStepFactory(ZipExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(ZIP_EXPORT)
                .tasklet(tasklet)
                .build();
    }
}
