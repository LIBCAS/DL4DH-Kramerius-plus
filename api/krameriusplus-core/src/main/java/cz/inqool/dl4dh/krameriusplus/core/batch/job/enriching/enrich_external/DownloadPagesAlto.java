package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.enrich_external;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.AltoContentExtractor;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.common.JobStep.DOWNLOAD_PAGES_ALTO;

@Configuration
@Slf4j
public class DownloadPagesAlto {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step downloadPagesAltoStep(ItemReader<Page> reader,
                                      ItemProcessor<Page, Page> downloadPagesAltoProcessor,
                                      MongoItemWriter<Page> writer) {
        return stepBuilderFactory.get(DOWNLOAD_PAGES_ALTO)
                .<Page, Page> chunk(10)
                .reader(reader)
                .processor(downloadPagesAltoProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Page, Page> downloadPagesAltoProcessor(StreamProvider streamProvider, AltoMapper altoMapper) {
        return page -> {
            Alto alto = streamProvider.getAlto(page.getId());

            if (alto == null) {
                log.debug("No ALTO for page {}", page.getId());
                return null;
            }

            AltoDto altoDto = altoMapper.toAltoDto(alto);
            new AltoContentExtractor(page).enrichPage(altoDto);

            return page;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
