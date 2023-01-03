package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.CleanUpExportTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.CLEAN_UP_EXPORT_STEP;

@Component
public class CleanUpExportStepDesigner extends AbstractStepDesigner {

    private CleanUpExportTasklet tasklet;

    @Bean(CLEAN_UP_EXPORT_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return CLEAN_UP_EXPORT_STEP;
    }

    @Autowired
    public void setTasklet(CleanUpExportTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
