package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.alto;

import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.MissingAltoOption;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.NUMBER_OF_ITEMS;
import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.MISSING_ALTO_STRATEGY;

@Component
public class MissingAltoStrategyFactory {

    public MissingAltoStrategy create(StepExecution stepExecution) {
        MissingAltoOption option = MissingAltoOption.valueOf(stepExecution.getJobExecution().getJobParameters()
                .getString(MISSING_ALTO_STRATEGY));

        long totalNumberOfPages = stepExecution.getExecutionContext().getLong(NUMBER_OF_ITEMS);

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
