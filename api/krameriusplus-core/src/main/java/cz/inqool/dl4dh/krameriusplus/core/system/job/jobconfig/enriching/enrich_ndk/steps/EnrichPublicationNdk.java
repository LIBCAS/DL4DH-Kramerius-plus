package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.enrich_ndk.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.dataprovider.kramerius.StreamProvider;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.publication.metadata.ModsWrapper;
import cz.inqool.dl4dh.mods.ModsCollectionDefinition;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.common.JobStep.ENRICH_PUBLICATION_NDK;

@Configuration
public class EnrichPublicationNdk {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step enrichPublicationNdkStep(ItemReader<Publication> publicationReader,
                                         ItemProcessor<Publication, Publication> enrichPublicationNdkProcessor,
                                         MongoItemWriter<Publication> writer) {
        return stepBuilderFactory.get(ENRICH_PUBLICATION_NDK)
                .<Publication, Publication>chunk(1)
                .reader(publicationReader)
                .processor(enrichPublicationNdkProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Publication, Publication> enrichPublicationNdkProcessor(StreamProvider streamProvider) {
        return publication -> {
            ModsCollectionDefinition mods = streamProvider.getMods(publication.getId());

            publication.setModsMetadata(new ModsWrapper(mods).getTransformedMods());

            return publication;
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
