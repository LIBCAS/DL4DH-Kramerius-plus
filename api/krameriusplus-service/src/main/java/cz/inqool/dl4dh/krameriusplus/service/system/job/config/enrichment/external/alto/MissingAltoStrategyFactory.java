package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Component
public class MissingAltoStrategyFactory {

    private final PublicationStore publicationStore;

    @Autowired
    public MissingAltoStrategyFactory(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    public MissingAltoStrategy create(StepExecution stepExecution, String publicationId) {
        MissingAltoOption option = MissingAltoOption.valueOf(stepExecution.getJobExecution().getJobParameters()
                .getString(MISSING_ALTO_STRATEGY));

        long totalNumberOfPages = publicationStore.countById(publicationId);

        switch (option) {
            case SKIP:
                return new SkipStrategy();
            case FAIL_IF_ONE_MISS:
                return new FailIfOneMissingStrategy();
            case FAIL_IF_ALL_MISS:
                return new FailIfAllMissingStrategy(totalNumberOfPages);
        }

        throw new IllegalStateException("Factory can't create strategy for option " + option);
    }
}
