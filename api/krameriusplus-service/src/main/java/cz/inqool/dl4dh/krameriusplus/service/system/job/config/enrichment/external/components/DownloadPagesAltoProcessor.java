package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto.MissingAltoStrategyFactory;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.AltoWrappedPageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, AltoWrappedPageDto> {

    private final StreamProvider streamProvider;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private boolean isParadataExtracted = false;

    private final MissingAltoStrategyFactory missingAltoStrategyFactory;

    private final MissingAltoOption missingAltoOption;

    private MissingAltoStrategy missingAltoStrategy;

    private String currentParentId;

    private Long missingAltoCounter = 0L;

    private final PublicationStore publicationStore;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider,
                                      AltoMetadataExtractor altoMetadataExtractor,
                                      MissingAltoStrategyFactory missingAltoStrategyFactory,
                                      PublicationStore publicationStore,
                                      @Value("#{jobParameters['" + MISSING_ALTO_STRATEGY + "']}") MissingAltoOption missingAltoOption) {
        this.streamProvider = streamProvider;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.missingAltoStrategyFactory = missingAltoStrategyFactory;
        this.publicationStore = publicationStore;
        this.missingAltoOption = missingAltoOption;
    }

    @Override
    public AltoWrappedPageDto process(@NonNull Page item) {
        if (!item.getParentId().equals(currentParentId)) {
            reportMissingAlto(currentParentId);
            currentParentId = item.getParentId();
            missingAltoStrategy = missingAltoStrategyFactory.create(missingAltoOption, currentParentId);
            isParadataExtracted = false;
        }
        try {
            Alto alto = streamProvider.getAlto(item.getId());

            if (alto == null) {
                handleMissingAlto(item);
                return null;
            }

            AltoWrappedPageDto dto = new AltoWrappedPageDto(item);
            dto.setAltoLayout(alto.getLayout());
            dto.setContent(altoMetadataExtractor.extractText(alto));

            if (!isParadataExtracted) {
                OCREnrichmentParadata paradata = altoMetadataExtractor.extractOcrParadata(alto);

                if (paradata != null) {
                    Publication publication = publicationStore.findById(item.getParentId()).orElseThrow(() -> new IllegalStateException("Page should always have a parent in db"));
                    publication.getParadata().put(paradata.getExternalSystem(), paradata);
                    publicationStore.save(publication);
                    isParadataExtracted = true;
                }
            }

            return dto;
        } catch (KrameriusException e) {
            missingAltoCounter++;
            if (KrameriusException.ErrorCode.NOT_FOUND.equals(e.getErrorCode())) {
                handleMissingAlto(item);
            }
            return null;
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

    private void handleMissingAlto(Page item) {
        missingAltoStrategy.handleMissingAlto(item);
    }
}
