package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.download_pages_alto;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.Steps;
import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.AltoContentExtractor;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.alto.dto.AltoMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@StepScope
@Slf4j
@Named(Steps.DownloadPagesAltoStep.PROCESSOR_NAME)
@Component
class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Page, Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    @Autowired
    ItemProcessor(StreamProvider streamProvider, AltoMapper altoMapper) {
        this.streamProvider = streamProvider;
        this.altoMapper = altoMapper;
    }

    @Override
    public Page process(@NonNull Page page) {
        Alto alto = streamProvider.getAlto(page.getId());

        if (alto == null) {
            log.debug("No ALTO for page {}", page.getId());
            return page;
        }

        AltoDto altoDto = altoMapper.toAltoDto(alto);
        new AltoContentExtractor().enrichPage(page, altoDto);

        return page;
    }
}
