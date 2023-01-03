package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.designer;

import cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet.ZipDirectoryTasklet;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.batch.step.KrameriusStep.ZIP_DIRECTORY_STEP;

@Component
public class ZipDirectoryStepDesigner extends AbstractStepDesigner {

    private ZipDirectoryTasklet tasklet;

    @Bean(ZIP_DIRECTORY_STEP)
    @Override
    public Step build() {
        return getStepBuilder()
                .tasklet(tasklet)
                .build();
    }

    @Override
    protected String getStepName() {
        return ZIP_DIRECTORY_STEP;
    }

    @Autowired
    public void setTasklet(ZipDirectoryTasklet tasklet) {
        this.tasklet = tasklet;
    }
}
