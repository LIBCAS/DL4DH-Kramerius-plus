package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@JobScope
public class PublicationTaskPartitioner implements Partitioner {

    private final List<String> childPublicationIds = new ArrayList<>();

    @Autowired
    public PublicationTaskPartitioner(PublicationStore publicationStore,
                                      @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}") String root) {
        childPublicationIds.addAll(publicationStore.findAllEditions(root));
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new LinkedHashMap<>();

        for (String id : childPublicationIds) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.put(ExecutionContextKey.PUBLICATION_ID, id);
            result.put(id, executionContext);
        }

        return result;
    }
}
