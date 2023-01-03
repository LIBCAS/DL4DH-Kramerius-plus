package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.SaveExportFileTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.SAVE_EXPORT_FILE_STEP;

@Component
public class SaveExportFileStepDesigner extends AbstractStepDesigner {

    private SaveExportFileTasklet tasklet;

    @Bean(SAVE_EXPORT_FILE_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return SAVE_EXPORT_FILE_STEP;
    }

    @Autowired
    public void setTasklet(SaveExportFileTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
