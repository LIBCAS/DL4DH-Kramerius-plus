package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps;

import cz.inqool.dl4dh.krameriusplus.core.jms.EnrichMessage;
import cz.inqool.dl4dh.krameriusplus.core.jms.JmsProducer;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingStep.DOWNLOAD_PUBLICATION_CHILDREN;


@Slf4j
@Configuration
public class DownloadPublicationChildren {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step downloadPublicationChildrenStep(ItemReader<DigitalObject> downloadPublicationChildrenReader,
                                                ItemProcessor<DigitalObject, Page> downloadPublicationChildrenProcessor,
                                                MongoItemWriter<Page> pageWriter) {
        return stepBuilderFactory.get(DOWNLOAD_PUBLICATION_CHILDREN)
                .<DigitalObject, Page> chunk(20)
                .reader(downloadPublicationChildrenReader)
                .processor(downloadPublicationChildrenProcessor)
                .writer(pageWriter)
                .build();
    }

    @StepScope
    @Bean
    protected ItemReader<DigitalObject> downloadPublicationChildrenReader(DataProvider dataProvider, @Value("#{jobParameters['publicationId']}") String publicationId) {
        return new ItemReader<>() {

            private List<DigitalObject> digitalObjects;

            private int nextToReadIndex = 0;

            @Override
            public DigitalObject read() {
                if (digitalObjects == null) {
                    log.debug("Downloading children for publication with ID={}", publicationId);
                    digitalObjects = dataProvider.getDigitalObjectsForParent(publicationId);
                }

                if (nextToReadIndex == digitalObjects.size()) {
                    return null;
                }

                return digitalObjects.get(nextToReadIndex++);
            }
        };
    }

    @StepScope
    @Bean
    ItemProcessor<DigitalObject, Page> downloadPublicationChildrenProcessor(JmsProducer jmsProducer) {
        return digitalObject -> {
            if (digitalObject instanceof Page) {
                return (Page) digitalObject;
            } else if (digitalObject instanceof Publication) {
                jmsProducer.sendEnrichMessage(new EnrichMessage(digitalObject.getId(), Date.from(Instant.now())));
            }

            return null;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
