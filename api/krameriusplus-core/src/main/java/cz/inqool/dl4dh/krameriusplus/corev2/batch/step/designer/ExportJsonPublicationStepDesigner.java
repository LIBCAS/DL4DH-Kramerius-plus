package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.ExportJsonPublicationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_JSON_PUBLICATION_STEP;

@Component
public class ExportJsonPublicationStepDesigner extends AbstractStepDesigner {

    private ExportJsonPublicationTasklet tasklet;

    @Bean(EXPORT_JSON_PUBLICATION_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Autowired
    public void setTasklet(ExportJsonPublicationTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    protected String getStepName() {
        return EXPORT_JSON_PUBLICATION_STEP;
    }
}
