package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_CSV;
import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.EXPORT_PAGES_CSV_MASTER;

@Component
public class ExportPagesCsvMasterStepFactory extends PartitionedStepFactory {

    private Step exportPagesCsvStep;

    @Bean(EXPORT_PAGES_CSV_MASTER)
    @Override
    public Step build() {
        return getBuilder().build();
    }

    @Override
    protected String getStepName() {
        return EXPORT_PAGES_CSV_MASTER;
    }

    @Override
    protected Step getPartitionedStep() {
        return exportPagesCsvStep;
    }

    @Autowired
    public void setExportPagesCsvStep(@Qualifier(EXPORT_PAGES_CSV) Step exportPagesCsvStep) {
        this.exportPagesCsvStep = exportPagesCsvStep;
    }
}
