package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.CreateMergeJobTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.CREATE_MERGE_JOB_STEP;

@Component
public class CreateMergeJobStepDesigner extends AbstractStepDesigner {

    private CreateMergeJobTasklet tasklet;

    @Bean(CREATE_MERGE_JOB_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CREATE_MERGE_JOB_STEP;
    }

    @Autowired
    public void setTasklet(CreateMergeJobTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
