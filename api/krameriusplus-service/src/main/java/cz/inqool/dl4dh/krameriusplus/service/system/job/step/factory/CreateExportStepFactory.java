package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.CreateExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.CREATE_EXPORT;

@Component
public class CreateExportStepFactory extends AbstractStepFactory {

    private final CreateExportTasklet tasklet;

    @Autowired
    public CreateExportStepFactory(CreateExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(CREATE_EXPORT)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_EXPORT;
    }
}
