package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.corev2.kramerius.DataProvider;
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

    private DataProvider dataProvider;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        notNull(publicationId, () -> new IllegalStateException("EnrichModsTasklet used without PUBLICATION_ID key in JobParameters."));

//        ModsCollectionDefinition mods = streamProvider.getMods(item.getId());
//
//        item.setModsMetadata(new ModsWrapper(mods).getTransformedMods());

        return null;
    }

    @Autowired
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
