package cz.inqool.dl4dh.krameriusplus.core.batch.step.writer;

import cz.inqool.dl4dh.krameriusplus.core.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.core.digitalobject.publication.Publication;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

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

    @Bean(KrameriusItemWriter.NO_OP_ITEM_WRITER)
    @StepScope
    public ItemWriter<Object> noOpItemWriter() {
        return new ItemWriter<Object>() {
            @Override
            public void write(List<?> items) throws Exception {
                // no-op
            }
        };
    }

    public static class KrameriusItemWriter {
        public static final String NO_OP_ITEM_WRITER = "NO_OP_ITEM_WRITER";
    }
}
