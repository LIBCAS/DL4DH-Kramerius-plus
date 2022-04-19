package cz.inqool.dl4dh.krameriusplus.core.job.enriching.download.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.DataProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.DigitalObject;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.create.DownloadKStructureCreateDto;
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

import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.DOWNLOAD_PUBLICATION_CHILDREN;


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

    @Bean
    @StepScope
    public ItemReader<DigitalObject> downloadPublicationChildrenReader(
            DataProvider dataProvider,
            @Value("#{jobParameters['publicationId']}") String publicationId) {
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

    @Bean
    @StepScope
    public ItemProcessor<DigitalObject, Page> downloadPublicationChildrenProcessor(
            @Value("#{jobParameters['jobEventId']}") String jobEventId,
            JobEventService jobEventService) {
        return digitalObject -> {
            if (digitalObject instanceof Page) {
                return (Page) digitalObject;
            } else if (digitalObject instanceof Publication) {
                JobEventDto parent = new JobEventDto();
                parent.setId(jobEventId);
                DownloadKStructureCreateDto createDto = new DownloadKStructureCreateDto();
                createDto.setPublicationId(digitalObject.getId());
                createDto.setParent(parent);
                jobEventService.create(createDto);
            }

            return null;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
