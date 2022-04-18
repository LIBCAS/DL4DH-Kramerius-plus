package cz.inqool.dl4dh.krameriusplus.core.job.enriching.enrich_external.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.lindat.UDPipeService;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.JobStep.ENRICH_PAGES_UD_PIPE;

@Configuration
public class EnrichPagesUDPipe {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPagesUDPipeStep(ItemReader<Page> reader,
                                      ItemProcessor<Page, Page> enrichPagesUDPipeProcessor,
                                      MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(ENRICH_PAGES_UD_PIPE)
                .<Page, Page> chunk(5)
                .reader(reader)
                .processor(enrichPagesUDPipeProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Page, Page> enrichPagesUDPipeProcessor(UDPipeService udPipeService) {
        return page -> {
            udPipeService.tokenize(page);

            return page;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
