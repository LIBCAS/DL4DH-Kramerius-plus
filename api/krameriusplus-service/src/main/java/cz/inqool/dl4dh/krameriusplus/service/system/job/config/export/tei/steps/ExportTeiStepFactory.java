package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.tei.components.ExportTeiTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.EXPORT_TEI;

@Component
public class ExportTeiStepFactory extends AbstractStepFactory {

    private final ExportTeiTasklet tasklet;

    @Autowired
    public ExportTeiStepFactory(ExportTeiTasklet tasklet) {
        this.tasklet = tasklet;
    }

    @Override
    public Step build() {
        return stepBuilderFactory.get(EXPORT_TEI)
                .tasklet(tasklet)
                .build();
    }
}
