package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.kramerius.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class KrameriusPublicationReader implements ItemReader<Publication> {

    private final DataProvider dataProvider;

    private boolean isRead = false;

    private String publicationId;

    @Autowired
    public KrameriusPublicationReader(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Publication read() throws JobParametersInvalidException {
        if (isRead) {
            return null;
        }

        isRead = true;

        log.debug("Downloading publication with ID={}", publicationId);

        DigitalObject digitalObject = dataProvider.getDigitalObject(publicationId);
        if (!(digitalObject instanceof Publication)) {
            throw new JobParametersInvalidException("Received DigitalObject which is not a publication");
        }

        return (Publication) digitalObject;
    }

    @BeforeStep
    public void setPublicationId(StepExecution stepExecution) {
        this.publicationId = stepExecution.getJobExecution().getJobParameters().getString("publicationId");
    }
}
