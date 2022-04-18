package cz.inqool.dl4dh.krameriusplus.core.job.enriching.download;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.DOWNLOAD_PUBLICATION;

@Configuration
@Slf4j
public class DownloadPublication {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step downloadPublicationStep(ItemReader<Publication> downloadPublicationReader,
                                        MongoItemWriter<Publication> publicationWriter) {
        return stepBuilderFactory.get(DOWNLOAD_PUBLICATION)
                .<Publication, Publication> chunk(1)
                .reader(downloadPublicationReader)
                .writer(publicationWriter)
                .build();
    }

    @Bean
    @StepScope
    protected ItemReader<Publication> downloadPublicationReader(DataProvider dataProvider, @Value("#{jobParameters['publicationId']}") String publicationId) {
        return new ItemReader<>() {

            private boolean isRead = false;

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
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
