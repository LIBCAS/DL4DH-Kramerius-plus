package cz.inqool.dl4dh.krameriusplus.corev2.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.corev2.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class ItemWritersConfig {

    @Bean
    @StepScope
    public MongoItemWriter<Page> pageMongoWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<Page>()
                .template(mongoOperations)
                .collection("pages")
                .build();
    }

    @Bean
    @StepScope
    public MongoItemWriter<Publication> publicationMongoWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<Publication>()
                .collection("publications")
                .template(mongoOperations)
                .build();
    }
}
