package cz.inqool.dl4dh.krameriusplus.service.system.job.config.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportFormat;
import cz.inqool.dl4dh.krameriusplus.service.system.export.ExporterMediator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.JobParameterKey.*;

@Component
@StepScope
public class ExportTasklet implements Tasklet {

    private final ObjectMapper objectMapper;

    private final ExporterMediator exporterMediator;

    @Autowired
    public ExportTasklet(ObjectMapper objectMapper, ExporterMediator exporterMediator) {
        this.objectMapper = objectMapper;
        this.exporterMediator = exporterMediator;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();
        String publicationId = jobParameters.getString(PUBLICATION_ID);
        Params params = objectMapper.readValue(jobParameters.getString(PARAMS), Params.class);
        ExportFormat exportFormat = ExportFormat.fromString(jobParameters.getString(EXPORT_FORMAT));

        exporterMediator.export(publicationId, params, exportFormat);

        return RepeatStatus.FINISHED;
    }
}
