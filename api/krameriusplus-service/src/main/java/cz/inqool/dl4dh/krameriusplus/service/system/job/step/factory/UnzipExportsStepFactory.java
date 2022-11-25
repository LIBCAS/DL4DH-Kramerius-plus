package cz.inqool.dl4dh.krameriusplus.service.system.job.step.factory;

import cz.inqool.dl4dh.krameriusplus.service.system.job.step.tasklet.UnzipExportsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.step.JobStep.UNZIP_EXPORTS;

@Component
public class UnzipExportsStepFactory extends AbstractStepFactory {

    private final UnzipExportsTasklet unzipExportsTasklet;

    @Autowired
    public UnzipExportsStepFactory(UnzipExportsTasklet unzipExportsTasklet) {
        this.unzipExportsTasklet = unzipExportsTasklet;
    }

    @Bean(UNZIP_EXPORTS)
    @Override
    public Step build() {
        return getBuilder().tasklet(unzipExportsTasklet).build();
    }

    @Override
    protected String getStepName() {
        return UNZIP_EXPORTS;
    }
}
