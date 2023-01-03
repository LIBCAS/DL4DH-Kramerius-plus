package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.MergeExportsTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.MERGE_EXPORTS_STEP;

@Component
public class MergeExportsStepDesigner extends AbstractStepDesigner {

    private MergeExportsTasklet mergeExportsTasklet;

    @Bean(MERGE_EXPORTS_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(mergeExportsTasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return MERGE_EXPORTS_STEP;
    }

    @Autowired
    public void setMergeExportsTasklet(MergeExportsTasklet mergeExportsTasklet) {
        this.mergeExportsTasklet = mergeExportsTasklet;
    }
}
