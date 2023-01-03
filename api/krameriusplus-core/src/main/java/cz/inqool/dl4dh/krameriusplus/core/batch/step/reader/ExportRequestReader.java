package cz.inqool.dl4dh.krameriusplus.core.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.ExportRequestStore;
import cz.inqool.dl4dh.krameriusplus.core.request.export.request.QExportRequest;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class ExportRequestReader extends RequestPublicationIdReader<ExportRequest, QExportRequest> {

    @Getter
    private ExportRequestStore requestStore;

    @Getter
    @Value("#{jobParameters['" + JobParameterKey.EXPORT_REQUEST_ID + "']}")
    private String requestId;

    @Autowired
    public void setRequestStore(ExportRequestStore requestStore) {
        this.requestStore = requestStore;
    }
}
