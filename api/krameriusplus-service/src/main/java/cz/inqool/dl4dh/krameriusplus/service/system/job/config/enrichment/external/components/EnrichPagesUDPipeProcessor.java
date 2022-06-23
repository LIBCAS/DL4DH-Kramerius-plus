package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.UDPipeService;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.UDPipeProcessDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.ExecutionContextKey.PARADATA;

@Component
@StepScope
public class EnrichPagesUDPipeProcessor implements ItemProcessor<Page, Page> {

    private final UDPipeService udPipeService;

    private ExecutionContext executionContext;

    private boolean isParadataExtracted = false;

    @Autowired
    public EnrichPagesUDPipeProcessor(UDPipeService udPipeService) {
        this.udPipeService = udPipeService;
    }

    @Override
    public Page process(@NonNull Page item) {
        UDPipeProcessDto response = udPipeService.processPage(item);
        item.setTokens(response.getTokens());

        if (!isParadataExtracted && response.getParadata() != null) {
            executionContext.put(PARADATA, response.getParadata());
            isParadataExtracted = true;
        }

        return item;
    }


    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.executionContext = stepExecution.getExecutionContext();
    }
}
