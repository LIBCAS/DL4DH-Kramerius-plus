package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.validation;

import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEventStore;
import cz.inqool.dl4dh.krameriusplus.service.system.dataprovider.kramerius.DataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class EnrichmentValidationConfiguration {

    @Bean
    @Order(1)
    public EnrichmentRequiredParamsValidator enrichmentRequiredParamsValidator() {
        return new EnrichmentRequiredParamsValidator();
    }

    @Bean
    @Autowired
    @Order(2)
    public OverrideValidator overrideValidator(JobEventStore jobEventStore) {
        return new OverrideValidator(jobEventStore);
    }

    @Bean
    @Autowired
    @Order(3)
    public UuidValidator uuidValidator(DataProvider dataProvider) {
        return new UuidValidator(dataProvider);
    }

    @Bean
    @Autowired
    @Order(4)
    public PublicationTypeValidator publicationTypeValidator(DataProvider dataProvider) {
        return new PublicationTypeValidator(dataProvider);
    }
}

