package cz.inqool.dl4dh.krameriusplus.service.system.job.config.enrichment.external.components;

import cz.inqool.dl4dh.krameriusplus.core.system.digitalobject.page.Page;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.NameTagService;
import cz.inqool.dl4dh.krameriusplus.service.system.enricher.page.lindat.dto.NameTagProcessDto;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
public class EnrichPagesNameTagProcessor implements ItemProcessor<Page, Page>, StepExecutionListener {

    private final NameTagService nameTagService;

    private ExecutionContext executionContext;

    private boolean isParadataExtracted = false;

    @Autowired
    public EnrichPagesNameTagProcessor(NameTagService nameTagService) {
        this.nameTagService = nameTagService;
    }

    @Override
    public Page process(@NonNull Page item) {
        NameTagProcessDto response = nameTagService.processPage(item.getTokens());
        item.setNameTagMetadata(response.getMetadata());

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

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
