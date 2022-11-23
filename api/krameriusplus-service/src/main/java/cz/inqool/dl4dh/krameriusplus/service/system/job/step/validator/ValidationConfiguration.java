package cz.inqool.dl4dh.krameriusplus.service.system.job.step.validator;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ValidationConfiguration {

    @Bean
    @Order(1)
    public EnrichmentRequiredParamsValidator enrichmentRequiredParamsValidator() {
        return new EnrichmentRequiredParamsValidator();
    }

    @Bean
    @Order(1)
    public ExportRequiredParamsValidator exportRequiredParamsValidator() {
        return new ExportRequiredParamsValidator();
    }

    @Bean
    @Order(2)
    public OverrideValidator overrideValidator(JobEventStore jobEventStore) {
        return new OverrideValidator(jobEventStore);
    }

    @Bean
    @Order(3)
    public UuidValidator uuidValidator(DataProvider dataProvider) {
        return new UuidValidator(dataProvider);
    }

    @Bean
    @Order(4)
    public PublicationTypeValidator publicationTypeValidator(DataProvider dataProvider) {
        return new PublicationTypeValidator(dataProvider);
    }
}

