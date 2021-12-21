package cz.inqool.dl4dh.krameriusplus.domain.dao.repo;

import cz.inqool.dl4dh.krameriusplus.domain.entity.scheduling.EnrichmentTask;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author Norbert Bodnar
 */
@Repository
public interface EnrichmentTaskRepository extends MongoRepository<EnrichmentTask, String> {

    @Query("{'state' : { $in : ?0}}")
    List<EnrichmentTask> findFinished(Collection<EnrichmentTask.State> states, PageRequest paging, Sort sort);

    List<EnrichmentTask> findEnrichmentTaskByPublicationId(String publicationId);
}
