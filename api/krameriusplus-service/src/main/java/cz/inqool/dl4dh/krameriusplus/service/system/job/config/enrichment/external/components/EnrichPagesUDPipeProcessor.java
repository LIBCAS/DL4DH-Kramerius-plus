package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.UDPipeService;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.UDPipeProcessDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesUDPipeProcessor implements ItemProcessor<EnrichPageFromAltoDto, EnrichPageFromAltoDto> {

    private final UDPipeService udPipeService;

    private boolean isParadataExtracted = false;

    private final PublicationStore publicationStore;

    private String currentParentId;

    @Autowired
    public EnrichPagesUDPipeProcessor(UDPipeService udPipeService, PublicationStore publicationStore) {
        this.udPipeService = udPipeService;
        this.publicationStore = publicationStore;
    }

    @Override
    public EnrichPageFromAltoDto process(@NonNull EnrichPageFromAltoDto item) {
        if (!item.getParentId().equals(currentParentId)) {
            currentParentId = item.getParentId();
            isParadataExtracted = false;
        }
        UDPipeProcessDto response = udPipeService.processPage(item);
        item.setTokens(response.getTokens());

        if (!isParadataExtracted && response.getParadata() != null) {
            Publication publication = publicationStore.findById(item.getParentId()).orElseThrow(() -> new IllegalStateException("Page always has a parent in db"));
            publication.getParadata().put(response.getParadata().getExternalSystem(), response.getParadata());
            publicationStore.save(publication);
            isParadataExtracted = true;
        }

        return item;
    }
}
