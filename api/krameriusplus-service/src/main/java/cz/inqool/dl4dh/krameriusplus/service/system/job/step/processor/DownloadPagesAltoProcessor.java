package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.KrameriusException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.alto.MissingAltoStrategy;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.AltoWrappedPageDto;
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

    private MissingAltoStrategy missingAltoStrategy;

    private Long missingAltoCounter = 0L;

    private final PublicationStore publicationStore;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider,
                                      AltoMetadataExtractor altoMetadataExtractor,
                                      PublicationStore publicationStore,
                                      @Value("#{jobParameters['" + MISSING_ALTO_STRATEGY + "']}") MissingAltoOption missingAltoOption) {
        this.streamProvider = streamProvider;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.publicationStore = publicationStore;
    }

    @Override
    public AltoWrappedPageDto process(@NonNull Page item) {
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

    private void handleMissingAlto(Page item) {
        missingAltoStrategy.handleMissingAlto(item);
    }
}
