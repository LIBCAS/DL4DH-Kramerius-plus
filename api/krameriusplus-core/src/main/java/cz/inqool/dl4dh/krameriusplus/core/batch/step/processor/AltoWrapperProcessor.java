package cz.inqool.dl4dh.krameriusplus.core.batch.step.processor;

import cz.inqool.dl4dh.alto.Alto;
import cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException;
import cz.inqool.dl4dh.krameriusplus.api.publication.paradata.OCREnrichmentParadata;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.AltoWrappedPage;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.enricher.alto.AltoMetadataExtractor;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.api.exception.EnrichingException.ErrorCode.MISSING_ALTO;

@Component
@StepScope
public class AltoWrapperProcessor implements ItemProcessor<Page, AltoWrappedPage> {

    private final KrameriusMessenger krameriusMessenger;

    private final AltoMetadataExtractor altoMetadataExtractor;

    private final PublicationStore publicationStore;

    private boolean isParadataExtracted = false;

    @Autowired
    public AltoWrapperProcessor(KrameriusMessenger krameriusMessenger,
                                AltoMetadataExtractor altoMetadataExtractor,
                                PublicationStore publicationStore) {
        this.krameriusMessenger = krameriusMessenger;
        this.altoMetadataExtractor = altoMetadataExtractor;
        this.publicationStore = publicationStore;
    }

    @Override
    public AltoWrappedPage process(@NonNull Page item) {
        Alto alto = krameriusMessenger.getAlto(item.getId());
        Utils.notNull(alto, () -> new EnrichingException(
                "Page number: " + item.getPageNumber() + ", with ID:" + item.getId() + " has no ALTO stream.",
                MISSING_ALTO));

        AltoWrappedPage wrappedPage = new AltoWrappedPage(item);
        wrappedPage.setAltoLayout(alto.getLayout());
        wrappedPage.setContent(altoMetadataExtractor.extractText(alto));

        if (!isParadataExtracted) {
            OCREnrichmentParadata paradata = altoMetadataExtractor.extractOcrParadata(alto);

            if (paradata != null) {
                Publication publication = publicationStore.findById(item.getParentId())
                        .orElseThrow(() -> new IllegalStateException("Page " + item.getId() + " has no parent publication in DB, " +
                                "could not save paradata."));
                publication.getParadata().put(paradata.getExternalSystem(), paradata);
                publicationStore.save(publication);
                isParadataExtracted = true;
            }
        }

        return wrappedPage;
    }
}
