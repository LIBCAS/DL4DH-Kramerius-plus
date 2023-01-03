package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.PrepareExportMetadataTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.PREPARE_EXPORT_METADATA_STEP;

@Component
public class PrepareExportMetadataStepDesigner extends AbstractStepDesigner {

    private PrepareExportMetadataTasklet tasklet;

    @Bean(PREPARE_EXPORT_METADATA_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return PREPARE_EXPORT_METADATA_STEP;
    }

    @Autowired
    public void setTasklet(PrepareExportMetadataTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
