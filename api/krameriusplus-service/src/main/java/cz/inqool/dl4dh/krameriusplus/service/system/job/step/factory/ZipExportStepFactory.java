package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.ZipExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.ZIP_EXPORT;

@Component
public class ZipExportStepFactory extends AbstractStepFactory {

    private final ZipExportTasklet tasklet;

    @Autowired
    public ZipExportStepFactory(ZipExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(ZIP_EXPORT)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return ZIP_EXPORT;
    }
}
