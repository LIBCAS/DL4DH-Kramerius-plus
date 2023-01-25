package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.core.enricher.mods.ModsMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class EnrichModsTasklet implements Tasklet {

    @Value("#{jobParameters['" + JobParameterKey.PUBLICATION_ID + "']}")
    private String publicationId;

    private KrameriusMessenger krameriusMessenger;

    private ModsMapper modsMapper;

    private PublicationStore publicationStore;

    private MongoOperations mongoOperations;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Utils.notNull(publicationId, () -> new IllegalStateException("EnrichModsTasklet used without PUBLICATION_ID key in JobParameters."));

        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        ModsCollectionDefinition mods = krameriusMessenger.getMods(publicationId);

        publication.setModsMetadata(modsMapper.map(mods));

        // When calling publicationStore.save(publication), the field modsMetadata comes in as NULL for some reason
        // and is therefore not saved to DB
        // publicationStore.save(publication);

        mongoOperations.save(publication, "publications");

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setKrameriusMessenger(KrameriusMessenger krameriusMessenger) {
        this.krameriusMessenger = krameriusMessenger;
    }

    @Autowired
    public void setPublicationStore(PublicationStore publicationStore) {
        this.publicationStore = publicationStore;
    }

    @Autowired
    public void setModsMapper(ModsMapper modsMapper) {
        this.modsMapper = modsMapper;
    }

    @Autowired
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }
}
