package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.CreateBulkFileRefTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.CREATE_BULK_FILE_REF;

@Component
public class CreateBulkFileRefStepFactory extends AbstractStepFactory {

    private final Tasklet tasklet;

    @Autowired
    public CreateBulkFileRefStepFactory(CreateBulkFileRefTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Bean(CREATE_BULK_FILE_REF)
    @Override
    public Step build() {
        return getBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_BULK_FILE_REF;
    }
}
