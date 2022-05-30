package cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.enriching.enrich_tei.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.tei.TeiConnector;
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

import static cz.inqool.dl4dh.krameriusplus.service.system.job.jobconfig.common.JobStep.ENRICH_PAGES_TEI;

@Configuration
@Slf4j
public class EnrichPagesTei {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesTeiStep(ItemReader<Page> reader,
                                   ItemProcessor<Page, Page> enrichPagesTeiProcessor,
                                   MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(ENRICH_PAGES_TEI)
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(enrichPagesTeiProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Page, Page> enrichPagesTeiProcessor(TeiConnector teiConnector, FileService fileService) {
        return page -> {
            String teiPage = teiConnector.convertToTeiPage(page);

            byte[] teiPageBytes = teiPage.getBytes(StandardCharsets.UTF_8);

            try (ByteArrayInputStream is = new ByteArrayInputStream(teiPageBytes)) {
                log.debug("Saving TEI page to a file for page {}", page.getId());

                FileRef fileRef = fileService.create(
                        is,
                        teiPageBytes.length,
                        page.getId() + "_tei_page.xml",
                        ContentType.APPLICATION_XML.getMimeType());

                page.setTeiBodyFileId(fileRef.getId());
            }

            return page;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
