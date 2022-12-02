package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.ModsMapper;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.store.PublicationStore;
import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.KrameriusMessenger;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
@StepScope
public class EnrichModsTasklet implements Tasklet {

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    private KrameriusMessenger krameriusMessenger;

    private ModsMapper modsMapper;

    private PublicationStore publicationStore;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        notNull(publicationId, () -> new IllegalStateException("EnrichModsTasklet used without PUBLICATION_ID key in JobParameters."));

        Publication publication = publicationStore.findById(publicationId)
                .orElseThrow(() -> new MissingObjectException(Publication.class, publicationId));

        ModsCollectionDefinition mods = krameriusMessenger.getMods(publicationId);

        publication.setModsMetadata(modsMapper.map(mods));

        return null;
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
}
