package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PARADATA;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private ExecutionContext executionContext;

    private boolean isParadataExtracted = false;

    private final MissingAltoStrategyFactory missingAltoStrategyFactory;

    private MissingAltoStrategy missingAltoStrategy;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider, AltoMapper altoMapper,
                                      AltoMetadataExtractor altoMetadataExtractor,
                                      MissingAltoStrategyFactory missingAltoStrategyFactory) {
        this.streamProvider = streamProvider;
        this.altoMapper = altoMapper;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.missingAltoStrategyFactory = missingAltoStrategyFactory;
    }

    @Override
    public Page process(@NonNull Page item) {
        try {
            Alto alto = streamProvider.getAlto(item.getId());

            if (alto == null) {
                handleMissingAlto(item);
            }

            AltoDto altoDto = altoMapper.toAltoDto(alto);

            item.setContent(altoMetadataExtractor.extractText(altoDto));
            item.setAltoLayout(altoDto.getLayout());

            if (!isParadataExtracted) {
                OCREnrichmentParadata paradata = altoMetadataExtractor.extractOcrParadata(altoDto);

                if (paradata != null) {
                    executionContext.put(PARADATA, paradata);
                    isParadataExtracted = true;
                }
            }

            return item;
        } catch (KrameriusException e) {
            if (KrameriusException.ErrorCode.NOT_FOUND.equals(e.getErrorCode())) {
                return handleMissingAlto(item);
            } else {
                throw e;
            }
        }
    }

    private Page handleMissingAlto(Page item) {
        return missingAltoStrategy.handleMissingAlto(item);
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
        this.missingAltoStrategy = missingAltoStrategyFactory.create(stepExecution);
    }
}
