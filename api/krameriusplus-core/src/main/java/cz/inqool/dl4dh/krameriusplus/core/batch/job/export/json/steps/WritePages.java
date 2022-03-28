package cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cz.inqool.dl4dh.krameriusplus.core.batch.job.export.json.steps.JsonExportingSteps.WRITE_PAGES;

@Configuration
public class WritePages {

    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step writePagesStep(ItemReader<Page> reader,
                               JsonFileItemWriter<Page> writer) {
        return stepBuilderFactory.get(WRITE_PAGES)
                .<Page, Page> chunk(5)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Autowired
    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }
}
