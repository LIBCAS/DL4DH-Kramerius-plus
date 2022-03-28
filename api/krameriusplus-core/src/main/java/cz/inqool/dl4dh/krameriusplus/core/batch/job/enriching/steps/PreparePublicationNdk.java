package cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.enricher.page.mets.MetsFileFinder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.Optional;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.enriching.steps.EnrichingStep.PREPARE_PUBLICATION_NDK;

@Configuration
public class PreparePublicationNdk {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step preparePublicationNdkStep(ItemReader<Publication> publicationReader,
                                          ItemProcessor<Publication, Publication> preparePublicationNdkProcessor,
                                          MongoItemWriter<Publication> publicationWriter) {
        return stepBuilderFactory.get(PREPARE_PUBLICATION_NDK)
                .<Publication, Publication>chunk(1)
                .reader(publicationReader)
                .processor(preparePublicationNdkProcessor)
                .writer(publicationWriter)
                .build();
    }

    @Bean
    @StepScope
    protected ItemProcessor<Publication, Publication> preparePublicationNdkProcessor(MetsFileFinder metsFileFinder) {
        return publication -> {
            Optional<Path> mainMetsPath = metsFileFinder.findNdkPublicationDirectory(publication.getId());

            if (mainMetsPath.isPresent()) {
                publication.setNdkDirPath(mainMetsPath.get().toString());

                return publication;
            }

            throw new IllegalStateException("NDK directory not found for publication " + publication.getId());
        };
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
