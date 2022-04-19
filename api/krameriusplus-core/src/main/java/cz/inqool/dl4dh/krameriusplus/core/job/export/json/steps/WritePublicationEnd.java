package cz.inqool.dl4dh.krameriusplus.core.job.export.json.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.job.enriching.common.PublicationFileWriter;
import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.publication.Publication;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WritePublicationEnd {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step writePublicationEndStep(ItemReader<Publication> publicationReader,
                                        ObjectMapper objectMapper) {
        return stepBuilderFactory.get(JsonExportingSteps.WRITE_PUBLICATION_END)
                .<Publication, Publication> chunk(1)
                .reader(publicationReader)
                .writer(new PublicationFileWriter(PublicationFileWriter.WritePart.AFTER_PAGES, objectMapper))
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
