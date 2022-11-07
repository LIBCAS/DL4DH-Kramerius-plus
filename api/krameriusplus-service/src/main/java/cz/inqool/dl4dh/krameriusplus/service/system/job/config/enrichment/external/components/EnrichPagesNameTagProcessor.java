package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.NameTagService;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.NameTagProcessDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.dto.EnrichPageFromAltoDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichPagesNameTagProcessor implements ItemProcessor<EnrichPageFromAltoDto, EnrichPageFromAltoDto> {

    private final NameTagService nameTagService;

    private boolean isParadataExtracted = false;

    private final PublicationStore publicationStore;

    private String currentParentId;

    @Autowired
    public EnrichPagesNameTagProcessor(NameTagService nameTagService, PublicationStore publicationStore) {
        this.nameTagService = nameTagService;
        this.publicationStore = publicationStore;
    }

    @Override
    public EnrichPageFromAltoDto process(@NonNull EnrichPageFromAltoDto item) {
        if (!item.getParentId().equals(currentParentId)) {
            currentParentId = item.getParentId();
            isParadataExtracted = false;
        }
        NameTagProcessDto response = nameTagService.processPage(item.getTokens());
        item.setNameTagMetadata(response.getMetadata());

        if (!isParadataExtracted && response.getParadata() != null) {
            Publication publication = publicationStore.findById(item.getParentId()).orElseThrow(() -> new IllegalStateException("Page always has a parent in db"));
            publication.getParadata().put(response.getParadata().getExternalSystem(), response.getParadata());
            isParadataExtracted = true;
        }

        return item;
    }
}
