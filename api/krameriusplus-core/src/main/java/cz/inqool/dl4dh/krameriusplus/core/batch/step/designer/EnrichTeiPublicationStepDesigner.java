package cz.inqool.dl4dh.krameriusplus.core.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet.EnrichTeiPublicationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.ENRICH_TEI_PUBLICATION_STEP;

@Component
public class EnrichTeiPublicationStepDesigner extends AbstractStepDesigner {

    private EnrichTeiPublicationTasklet tasklet;

    @Bean(ENRICH_TEI_PUBLICATION_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return ENRICH_TEI_PUBLICATION_STEP;
    }

    @Autowired
    public void setEnrichTeiPublicationTasklet(EnrichTeiPublicationTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
