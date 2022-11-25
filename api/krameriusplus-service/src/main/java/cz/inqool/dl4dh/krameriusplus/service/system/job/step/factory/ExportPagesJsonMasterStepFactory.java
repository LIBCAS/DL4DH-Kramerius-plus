package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_JSON;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_JSON_MASTER;

@Component
public class ExportPagesJsonMasterStepFactory extends PartitionedStepFactory {

    private Step exportPagesJsonStep;

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_JSON_MASTER;
    }

    @Override
    protected Step getPartitionedStep() {
        return exportPagesJsonStep;
    }

    @Bean(EXPORT_PAGES_JSON_MASTER)
    @Override
    public Step build() {
        return getBuilder().build();
    }

    @Autowired
    public void setExportPagesJsonStep(@Qualifier(EXPORT_PAGES_JSON) Step exportPagesJsonStep) {
        this.exportPagesJsonStep = exportPagesJsonStep;
    }
}
