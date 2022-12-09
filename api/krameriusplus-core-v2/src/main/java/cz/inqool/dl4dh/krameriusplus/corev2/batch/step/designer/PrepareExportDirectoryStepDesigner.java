package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.PrepareExportDirectoryTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.PREPARE_EXPORT_DIRECTORY_STEP;

@Component
public class PrepareExportDirectoryStepDesigner extends AbstractStepDesigner {

    private PrepareExportDirectoryTasklet tasklet;

    @Bean(PREPARE_EXPORT_DIRECTORY_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return PREPARE_EXPORT_DIRECTORY_STEP;
    }

    @Autowired
    public void setTasklet(PrepareExportDirectoryTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
