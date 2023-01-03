package cz.inqool.dl4dh.krameriusplus.core.batch;

import org.springframework.batch.core.Step;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ExportTeiJobDesigner extends AbstractExportJobDesigner {
    @Override
    protected List<Step> getExportSteps() {
        return null;
    }

    @Override
    public String getJobName() {
        return null;
    }
}
