package cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.export.json.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import cz.inqool.dl4dh.krameriusplus.core.system.job.jobconfig.enriching.common.PublicationFileWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WritePublicationStart {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step writePublicationStartStep(ItemReader<Publication> publicationReader,
                                          ObjectMapper objectMapper) {
        return stepBuilderFactory.get(JsonExportingSteps.WRITE_PUBLICATION_START)
                .<Publication, Publication> chunk(1)
                .reader(publicationReader)
                .writer(new PublicationFileWriter(PublicationFileWriter.WritePart.BEFORE_PAGES, objectMapper))
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
