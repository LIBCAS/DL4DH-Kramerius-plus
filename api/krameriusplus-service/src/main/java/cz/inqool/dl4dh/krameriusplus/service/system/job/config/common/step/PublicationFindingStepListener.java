package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@JobScope
public class PublicationFindingStepListener implements StepExecutionListener {

    private final List<String> ids = new ArrayList<>();

    private String next;


    @Autowired
    public PublicationFindingStepListener(PublicationStore publicationStore,
                                          @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}") String root) {
        ids.addAll(publicationStore.findAllEditions(root));
        next = ids.get(0);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        stepExecution.getExecutionContext().put(ExecutionContextKey.PUBLICATION_ID, next);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        next = findNext();
        if (next != null) {
            return new ExitStatus("CONTINUE");
        }
        return null;
    }

    private String findNext() {
        int lastProcessedIndex = ids.indexOf(next);
        return lastProcessedIndex + 1 < ids.size() ? ids.get(ids.indexOf(next) + 1) : null;
    }
}
