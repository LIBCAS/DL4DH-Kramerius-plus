package cz.inqool.dl4dh.krameriusplus.corev2.batch;

import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType.KrameriusJobTypeName.CREATE_EXPORT_REQUEST;

@Component
public class CreateExportRequestJobDesigner extends AbstractJobDesigner {

    @Override
    public String getJobName() {
        return CREATE_EXPORT_REQUEST;
    }

    @Bean(CREATE_EXPORT_REQUEST)
    @Override
    public Job build() {
        //TODO: write sequence of steps
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
