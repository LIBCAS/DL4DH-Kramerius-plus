package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoDto;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.alto.AltoMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoStrategy;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException.ErrorCode.MISSING_STREAM;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.NUMBER_OF_ITEMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PARADATA;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, Page> {

    private final StreamProvider streamProvider;

    private final AltoMapper altoMapper;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private ExecutionContext executionContext;

    private boolean isParadataExtracted = false;

    private MissingAltoStrategy missingAltoStrategy;

    private long missingAltoCounter = 0;

    private long totalPages;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider, AltoMapper altoMapper,
                                      AltoMetadataExtractor altoMetadataExtractor) {
        this.streamProvider = streamProvider;
        this.altoMapper = altoMapper;
        this.altoMetadataExtractor = altoMetadataExtractor;
    }

    @Override
    public Page process(@NonNull Page item) {
        try {
            Alto alto = streamProvider.getAlto(item.getId());

            if (alto == null) {
                log.debug("No ALTO for page {}", item.getId());
                return null;
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
                return handleMissingAlto(item, e);
            } else {
                throw e;
            }
        }
    }

    private Page handleMissingAlto(Page item, KrameriusException e) {
        missingAltoCounter++;
        switch (missingAltoStrategy) {
            case SKIP:
                return item;
            case FAIL_IF_ONE_MISS:
                throw new KrameriusException(MISSING_STREAM, e,
                        "No ALTO found for page '" + item.getId() + "' and strategy FAIL_IF_ONE_MISS is selected.");
            case FAIL_IF_ALL_MISS:
                if (totalPages == missingAltoCounter) {
                    throw new KrameriusException(MISSING_STREAM, e,
                            "No ALTO found for every page");
                } else {
                    return item;
                }
        }

        return item;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
        this.missingAltoStrategy = MissingAltoStrategy.valueOf(stepExecution
                .getJobExecution().getJobParameters().getString(MISSING_ALTO_STRATEGY));
        this.totalPages = stepExecution.getExecutionContext().getLong(NUMBER_OF_ITEMS);
    }
}
