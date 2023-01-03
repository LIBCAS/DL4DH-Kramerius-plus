package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.ExportTeiFinishTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.EXPORT_TEI_FINISH_STEP;

@Component
public class ExportTeiFinishStepDesigner extends AbstractStepDesigner {

    private ExportTeiFinishTasklet tasklet;

    @Bean(EXPORT_TEI_FINISH_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_TEI_FINISH_STEP;
    }

    @Autowired
    public void setTasklet(ExportTeiFinishTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
