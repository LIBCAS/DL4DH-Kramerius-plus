package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.ExportItemsEnqueuingTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.ENQUEUE_EXPORT_ITEMS_STEP;

@Component
public class EnqueueExportItemsStepDesigner extends AbstractStepDesigner {

    private ExportItemsEnqueuingTasklet tasklet;

    @Bean(ENQUEUE_EXPORT_ITEMS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return ENQUEUE_EXPORT_ITEMS_STEP;
    }

    @Autowired
    public void setTasklet(ExportItemsEnqueuingTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
