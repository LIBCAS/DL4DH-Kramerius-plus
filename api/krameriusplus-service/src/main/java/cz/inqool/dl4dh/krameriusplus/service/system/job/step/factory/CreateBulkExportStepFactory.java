package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.CreateBulkExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.CREATE_BULK_EXPORT;

@Component
public class CreateBulkExportStepFactory extends AbstractStepFactory {

    private final CreateBulkExportTasklet tasklet;

    @Autowired
    public CreateBulkExportStepFactory(CreateBulkExportTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(CREATE_BULK_EXPORT)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_BULK_EXPORT;
    }
}
