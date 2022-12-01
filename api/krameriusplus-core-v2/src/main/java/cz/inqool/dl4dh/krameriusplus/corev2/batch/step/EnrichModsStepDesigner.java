package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.EnrichModsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_MODS_STEP;

@Component
public class EnrichModsStepDesigner extends AbstractStepDesigner {

    private EnrichModsTasklet tasklet;

    @Override
    protected String getStepName() {
        return ENRICH_MODS_STEP;
    }

    @Bean(ENRICH_MODS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Autowired
    public void setTasklet(EnrichModsTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
