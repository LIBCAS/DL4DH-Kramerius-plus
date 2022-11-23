package cz.inqool.dl4dh.krameriusplus.service.system.job;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.KrameriusJob;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.DOWNLOAD_DIGITAL_OBJECTS;

@Configuration
public class EnrichmentKrameriusJobDesigner extends EnrichmentJobDesigner {

    private Step downloadDigitalObjectsStep;

    @Bean
    public Job enrichingJob() {
        return super.getJobBuilder()
                .next(downloadDigitalObjectsStep)
                .build();
    }

    @Override
    public String getJobName() {
        return KrameriusJob.ENRICHMENT_KRAMERIUS.name();
    }

    @Autowired
    public void setDownloadDigitalObjectsStep(@Qualifier(DOWNLOAD_DIGITAL_OBJECTS) Step downloadDigitalObjectsStep) {
        this.downloadDigitalObjectsStep = downloadDigitalObjectsStep;
    }
}
