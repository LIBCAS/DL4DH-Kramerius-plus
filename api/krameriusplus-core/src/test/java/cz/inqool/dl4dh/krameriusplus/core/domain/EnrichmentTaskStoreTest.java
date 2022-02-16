package cz.inqool.dl4dh.krameriusplus.core.domain;

import cz.inqool.dl4dh.krameriusplus.core.domain.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.params.filter.EqFilter;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTask;
import cz.inqool.dl4dh.krameriusplus.core.system.scheduling.EnrichmentTaskStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@AutoConfigureDataMongo
public class EnrichmentTaskStoreTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private EnrichmentTaskStore enrichmentTaskStore;

    @BeforeEach
    void setUp() {
        enrichmentTaskStore = new EnrichmentTaskStore(mongoTemplate);
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void fetchWithSubtask() {
        EnrichmentTask enrichmentTask = new EnrichmentTask("1");

        enrichmentTaskStore.create(enrichmentTask);

        List<EnrichmentTask> result = enrichmentTaskStore.listAll();

        assertEquals(1, result.size());
        assertNotNull(result.get(0).getSubtask());
        assertEquals("1", result.get(0).getSubtask().getPublicationId());
    }

    @Test
    void fetchWithSubtaskByPublicationId() {
        enrichmentTaskStore.create(new EnrichmentTask("1"));
        enrichmentTaskStore.create(new EnrichmentTask("2"));
        enrichmentTaskStore.create(new EnrichmentTask("3"));

        Params params = new Params();
        params.addFilters(new EqFilter("subtask.publicationId", "2"));

        List<EnrichmentTask> result = enrichmentTaskStore.listAll(params);

        assertEquals(1, result.size());
        assertNotNull(result.get(0).getSubtask());
        assertEquals("2", result.get(0).getSubtask().getPublicationId());
    }
}
