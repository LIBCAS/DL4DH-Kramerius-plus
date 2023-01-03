package cz.inqool.dl4dh.krameriusplus.core.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.MERGE_JOB;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.KrameriusStep.MERGE_EXPORTS_STEP;

@Component
public class MergeExportsJobDesigner extends AbstractJobDesigner {

    private Step mergeStep;

    @Override
    public String getJobName() {
        return MERGE_JOB;
    }

    @Bean(MERGE_JOB)
    @Override
    public Job build() {
        return getJobBuilder()
                .start(mergeStep)
                .build();
    }

    @Autowired
    public void setMergeStep(@Qualifier(MERGE_EXPORTS_STEP) Step mergeStep) {
        this.mergeStep = mergeStep;
    }
}
