package cz.inqool.dl4dh.krameriusplus.core.job.enriching.enrich_tei.tei;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.tei.TeiConnector;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.ENRICH_PUBLICATION_TEI;

@Configuration
@Slf4j
public class EnrichPublicationTei {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPublicationTeiStep(ItemReader<Publication> publicationReader,
                                         ItemProcessor<Publication, Publication> enrichPublicationTeiProcessor,
                                         MongoItemWriter<Publication> writer) {
        return stepBuilderFactory.get(ENRICH_PUBLICATION_TEI)
                .<Publication, Publication>chunk(1)
                .reader(publicationReader)
                .processor(enrichPublicationTeiProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Publication, Publication> enrichPublicationTeiProcessor(TeiConnector teiConnector, FileService fileService) {
        return publication -> {
            String teiHeader = teiConnector.convertToTeiHeader(publication);

            byte[] teiHeaderBytes = teiHeader.getBytes(StandardCharsets.UTF_8);

            try (ByteArrayInputStream is = new ByteArrayInputStream(teiHeaderBytes)) {
                log.debug("Saving TEI HEADER to a file for publication {}", publication.getId());

                FileRef fileRef = fileService.create(is,
                        teiHeaderBytes.length,
                        publication.getId() + "_tei_header.xml",
                        ContentType.APPLICATION_XML.getMimeType());

                publication.setTeiHeaderFileId(fileRef.getId());
            }

            return publication;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
