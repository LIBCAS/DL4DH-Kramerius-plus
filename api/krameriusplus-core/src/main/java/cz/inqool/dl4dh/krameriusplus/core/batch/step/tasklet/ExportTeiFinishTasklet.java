package cz.inqool.dl4dh.krameriusplus.core.batch.step.tasklet;

import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.batch.step.wrapper.DigitalObjectExport;
import cz.inqool.dl4dh.krameriusplus.core.enricher.tei.TeiMessenger;
import cz.inqool.dl4dh.krameriusplus.core.exporter.DigitalObjectExporter;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportJobConfig;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportJobConfigStore;
import cz.inqool.dl4dh.krameriusplus.core.job.config.export.ExportTeiJobConfig;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.DIRECTORY;
import static cz.inqool.dl4dh.krameriusplus.core.batch.step.ExecutionContextKey.TEI_SESSION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.JOB_CONFIG_ID;
import static cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey.PUBLICATION_ID;
import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.isTrue;

@Component
@StepScope
public class ExportTeiFinishTasklet implements Tasklet {

    @Value("#{jobParameters['" + JOB_CONFIG_ID + "']}")
    private String jobConfigId;

    @Value("#{jobExecutionContext['" + TEI_SESSION_ID + "']}")
    private String sessionId;

    @Value("#{jobParameters['" + PUBLICATION_ID + "']}")
    private String publicationId;

    private Path directory;

    private TeiMessenger teiMessenger;

    private ExportJobConfigStore jobConfigStore;

    private DigitalObjectExporter exporter;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExportJobConfig jobConfig = jobConfigStore.findById(jobConfigId)
                .orElseThrow(() -> new MissingObjectException(ExportJobConfig.class, jobConfigId));
        isTrue(jobConfig instanceof ExportTeiJobConfig,
                () -> new IllegalStateException("Expected type of ExportTeiJobConfig, but found " + jobConfig.getClass().getSimpleName()));

        String result = teiMessenger.finishMerge(sessionId, ((ExportTeiJobConfig) jobConfig).getTeiParams());

        exporter.export(List.of(new DigitalObjectExport(null, result,
                publicationId.substring(5) + ".xml")), directory);

        return RepeatStatus.FINISHED;
    }

    @Autowired
    public void setTeiMessenger(TeiMessenger teiMessenger) {
        this.teiMessenger = teiMessenger;
    }

    @Autowired
    public void setJobConfigStore(ExportJobConfigStore jobConfigStore) {
        this.jobConfigStore = jobConfigStore;
    }

    @Autowired
    public void setDirectory(@Value("#{jobExecutionContext['" + DIRECTORY + "']}") String directory) {
        this.directory = Path.of(directory);
    }

    @Autowired
    public void setExporter(DigitalObjectExporter exporter) {
        this.exporter = exporter;
    }
}
