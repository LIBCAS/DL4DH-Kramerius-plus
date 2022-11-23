package cz.inqool.dl4dh.krameriusplus.service.system.job.step.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MissingAltoStrategyFactory {

    private final PublicationStore publicationStore;

    @Autowired
    public MissingAltoStrategyFactory(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    public MissingAltoStrategy create(MissingAltoOption missingAltoOption, String publicationId) {

        long totalNumberOfPages = publicationStore.findById(publicationId)
                .orElseThrow(() -> new IllegalStateException("Publication not found in AltoStrategy"))
                .getPageCount();

        switch (missingAltoOption) {
            case SKIP:
                return new SkipStrategy();
            case FAIL_IF_ONE_MISS:
                return new FailIfOneMissingStrategy();
            case FAIL_IF_ALL_MISS:
                return new FailIfAllMissingStrategy(totalNumberOfPages);
        }

        throw new IllegalStateException("Factory can't create strategy for option " + missingAltoOption);
    }
}
