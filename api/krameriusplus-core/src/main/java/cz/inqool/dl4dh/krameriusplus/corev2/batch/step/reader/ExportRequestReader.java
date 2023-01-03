package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.reader;

import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequest;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.ExportRequestStore;
import cz.inqool.dl4dh.krameriusplus.corev2.request.export.request.QExportRequest;
import lombok.Getter;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Component
@StepScope
public class ExportRequestReader extends RequestPublicationIdReader<ExportRequest, QExportRequest> {

    @Getter
    private ExportRequestStore requestStore;

    @Getter
    @Value("#{jobParameters['" + EXPORT_REQUEST_ID + "']}")
    private String requestId;

    @Autowired
    public void setRequestStore(ExportRequestStore requestStore) {
        this.requestStore = requestStore;
    }
}
