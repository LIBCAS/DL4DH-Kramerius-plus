package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategyFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, EnrichPageFromAltoDto>, StepExecutionListener {

    private final StreamProvider streamProvider;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private StepExecution stepExecution;

    private boolean isParadataExtracted = false;

    private final MissingAltoStrategyFactory missingAltoStrategyFactory;

    private MissingAltoStrategy missingAltoStrategy;

    private String currentParentId;

    private Long missingAltoCounter = 0L;

    private final PublicationStore publicationStore;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider,
                                      AltoMetadataExtractor altoMetadataExtractor,
                                      MissingAltoStrategyFactory missingAltoStrategyFactory, PublicationStore publicationStore) {
        this.streamProvider = streamProvider;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.missingAltoStrategyFactory = missingAltoStrategyFactory;
        this.publicationStore = publicationStore;
    }

    @Override
    public EnrichPageFromAltoDto process(@NonNull Page item) {
        EnrichPageFromAltoDto dto = new EnrichPageFromAltoDto(item);

        if (!dto.getPage().getParentId().equals(currentParentId)) {
            reportMissingAlto(currentParentId);
            currentParentId = dto.getPage().getParentId();
            missingAltoStrategy = missingAltoStrategyFactory.create(stepExecution, currentParentId);
            isParadataExtracted = false;
        }
        try {
            Alto alto = streamProvider.getAlto(dto.getPage().getId());

            if (alto == null) {
                handleMissingAlto(dto.getPage());
            }

            dto.setAltoLayout(alto == null ? null : alto.getLayout());
            dto.setContent(altoMetadataExtractor.extractText(alto));

            if (!isParadataExtracted) {
                OCREnrichmentParadata paradata = altoMetadataExtractor.extractOcrParadata(alto);

                if (paradata != null) {
                    Publication publication = publicationStore.findById(dto.getPage().getParentId()).orElseThrow(() -> new IllegalStateException("Page always has a parent in db"));
                    publication.getParadata().put(paradata.getExternalSystem(), paradata);
                    publicationStore.save(publication);
                    isParadataExtracted = true;
                }
            }

            return dto;
        } catch (KrameriusException e) {
            missingAltoCounter++;
            if (KrameriusException.ErrorCode.NOT_FOUND.equals(e.getErrorCode())) {
                return handleMissingAlto(dto.getPage()) == null ? null : dto;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            missingAltoCounter++;
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    private void reportMissingAlto(String currentParentId) {
        if (missingAltoCounter > 0) {
            log.warn(String.format("Processing of publication: %s, couldn't find/access %s ALTO items.",
                    currentParentId, missingAltoCounter));
        }

        missingAltoCounter = 0L;
    }

    private Page handleMissingAlto(Page item) {
        return missingAltoStrategy.handleMissingAlto(item);
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
