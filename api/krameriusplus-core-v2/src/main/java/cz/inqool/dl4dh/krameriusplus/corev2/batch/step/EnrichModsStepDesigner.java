package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.EnrichModsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ENRICH_MODS;

@Configuration
public class EnrichModsStepDesigner extends AbstractStepDesigner {

    private EnrichModsTasklet tasklet;

    @Override
    protected String getStepName() {
        return ENRICH_MODS;
    }

    @Bean(ENRICH_MODS)
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
