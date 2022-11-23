package cz.inqool.dl4dh.krameriusplus.service.system.job.step.processor;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.service.system.job.step.dto.AltoWrappedPageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.domain.exception.EnrichingException.ErrorCode.MISSING_ALTO;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
@StepScope
@Slf4j
public class DownloadPagesAltoProcessor implements ItemProcessor<Page, AltoWrappedPageDto> {

    private final StreamProvider streamProvider;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private boolean isParadataExtracted = false;

    private final PublicationStore publicationStore;

    @Autowired
    public DownloadPagesAltoProcessor(StreamProvider streamProvider,
                                      AltoMetadataExtractor altoMetadataExtractor,
                                      PublicationStore publicationStore) {
        this.streamProvider = streamProvider;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.publicationStore = publicationStore;
    }

    @Override
    public AltoWrappedPageDto process(@NonNull Page item) {
        Alto alto = streamProvider.getAlto(item.getId());
        notNull(alto, () -> new EnrichingException(
                "Page number: " + item.getPageNumber() + ", with ID:" + item.getId() + " has no ALTO stream.",
                MISSING_ALTO));

        AltoWrappedPageDto dto = new AltoWrappedPageDto(item);
        dto.setAltoLayout(alto.getLayout());
        dto.setContent(altoMetadataExtractor.extractText(alto));

        if (!isParadataExtracted) {
            OCREnrichmentParadata paradata = altoMetadataExtractor.extractOcrParadata(alto);

            if (paradata != null) {
                Publication publication = publicationStore.findById(item.getParentId())
                        .orElseThrow(() -> new IllegalStateException("Page should always have a parent in db"));
                publication.getParadata().put(paradata.getExternalSystem(), paradata);
                publicationStore.save(publication);
                isParadataExtracted = true;
            }
        }

        return dto;
    }
}
