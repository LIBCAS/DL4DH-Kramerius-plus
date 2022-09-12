package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.steps;

import cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.factory.AbstractStepFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.export.common.components.UnzipExportsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step.JobStep.UNZIP_EXPORTS;

@Component
public class UnzipExportsStepFactory extends AbstractStepFactory {

    private final UnzipExportsTasklet unzipExportsTasklet;

    @Autowired
    public UnzipExportsStepFactory(UnzipExportsTasklet unzipExportsTasklet) {
        this.unzipExportsTasklet = unzipExportsTasklet;
    }

    @Override
    protected String getStepName() {
        return UNZIP_EXPORTS;
    }

    @Override
    public Step build() {
        return getBuilder().tasklet(unzipExportsTasklet).build();
    }
}
