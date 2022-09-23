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
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PARADATA;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PUBLICATION_ID;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private StepExecution stepExecution;

    private boolean isParadataExtracted = false;

    private final MissingAltoStrategyFactory missingAltoStrategyFactory;

    private MissingAltoStrategy missingAltoStrategy;

    private String currentParentId;

    private Long missingAltoCounter = 0L;

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
        if (!item.getParentId().equals(currentParentId)) { // <- ak dostanes stranku s novym parentom, updatnes strategiu, ktora si vo factory dopocita count
            currentParentId = item.getParentId();
            missingAltoStrategy = missingAltoStrategyFactory.create(stepExecution, currentParentId);
        }
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
                    stepExecution.getExecutionContext().put(PARADATA, paradata);
                    isParadataExtracted = true;
                }
            }

            return item;
        } catch (KrameriusException e) {
            missingAltoCounter++;
            if (KrameriusException.ErrorCode.NOT_FOUND.equals(e.getErrorCode())) {
                return handleMissingAlto(item);
            } else {
                return null;
            }
        } catch (Exception e) {
            missingAltoCounter++;
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    private Page handleMissingAlto(Page item) {
        return missingAltoStrategy.handleMissingAlto(item);
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    // TODO: make a better strategy unaccessible ALTO for pages
    @AfterStep
    public void warnAfterProcessing(StepExecution stepExecution) {
        if (missingAltoCounter > 0) {
            log.warn(String.format("Processing of publication: %s, couldn't find/access %s ALTO items.",
                    stepExecution.getJobParameters().getString(PUBLICATION_ID), missingAltoCounter));
        }
    }
}
