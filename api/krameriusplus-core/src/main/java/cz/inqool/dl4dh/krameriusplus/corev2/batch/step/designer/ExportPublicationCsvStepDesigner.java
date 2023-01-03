package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.ExportCsvPublicationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.EXPORT_CSV_PUBLICATION_STEP;

@Component
public class ExportPublicationCsvStepDesigner extends AbstractStepDesigner {

    private ExportCsvPublicationTasklet tasklet;

    @Bean(EXPORT_CSV_PUBLICATION_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Autowired
    public void setTasklet(ExportCsvPublicationTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    protected String getStepName() {
        return EXPORT_CSV_PUBLICATION_STEP;
    }
}
