package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceGridDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepErrorDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepExecutionDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepError;
import cz.inqool.dl4dh.krameriusplus.core.job.report.StepRunReport;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(uses = {
        JobExplorer.class
})
public abstract class KrameriusJobInstanceMapper {

    private JobExplorer jobExplorer;

    public abstract KrameriusJobInstanceGridDto toGridDto(KrameriusJobInstance entity);

    @Mapping(target = "executions", expression = "java(getJobExecutions(entity))")
    public abstract KrameriusJobInstanceDto toDto(KrameriusJobInstance entity);

    @Mappings({
            @Mapping(target = "stepExecutions", expression = "java(mapStepExecutions(jobExecution.getStepExecutions(), stepRunReportMap))"),
            @Mapping(target = "exitCode", source = "jobExecution.exitStatus.exitCode"),
            @Mapping(target = "exitDescription", source = "jobExecution.exitStatus.exitDescription")
    })
    public abstract JobExecutionDto mapJobExecution(JobExecution jobExecution, Map<Long, StepRunReport> stepRunReportMap);

    public List<KrameriusJobInstanceGridDto> toGridDtoList(Map<Long, KrameriusJobInstance> entityMap) {
        return new TreeMap<>(entityMap).values()
                .stream()
                .map(this::toGridDto)
                .collect(Collectors.toList());
    }

    public JobParameters toJobParameters(KrameriusJobInstance entity, Map<String, Object> nonIdentifyingParams) {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString(JobParameterKey.KRAMERIUS_JOB_INSTANCE_ID, entity.getId());

        for (Map.Entry<String, Object> entry : nonIdentifyingParams.entrySet()) {
            if (entry.getValue() instanceof String) {
                parametersBuilder.addString(entry.getKey(), (String) entry.getValue(), false);
            } else if (entry.getValue() instanceof Date) {
                parametersBuilder.addDate(entry.getKey(), (Date) entry.getValue(), false);
            } else if (entry.getValue() instanceof Long) {
                parametersBuilder.addLong(entry.getKey(), (Long) entry.getValue(), false);
            } else if (entry.getValue() instanceof Double) {
                parametersBuilder.addDouble(entry.getKey(), (Double) entry.getValue(), false);
            } else {
                throw new IllegalArgumentException("Cannot save jobParameter of type: " + entry.getValue().getClass().getSimpleName());
            }
        }

        return parametersBuilder.toJobParameters();
    }

    @Mappings({
            @Mapping(target = "errors", expression = "java(mapStepRunReport(stepRunReport))"),
            @Mapping(target = "id", source = "stepExecution.id"),
            @Mapping(target = "exitCode", source = "stepExecution.exitStatus.exitCode"),
            @Mapping(target = "exitDescription", source = "stepExecution.exitStatus.exitDescription")
    })
    public abstract StepExecutionDto mapStepExecution(StepExecution stepExecution, StepRunReport stepRunReport);

    public abstract StepErrorDto toErrorDto(StepError stepError);

    protected List<JobExecutionDto> getJobExecutions(KrameriusJobInstance entity) {
        if (entity.getJobInstanceId() == null) {
            return new ArrayList<>();
        }

        JobInstance jobInstance = jobExplorer.getJobInstance(entity.getJobInstanceId());
        Utils.notNull(jobInstance, () -> new MissingObjectException(JobInstance.class, String.valueOf(entity.getJobInstanceId())));
        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);

        return jobExecutions.stream()
                .map(jobExecution -> mapJobExecution(jobExecution, entity.getReports()))
                .collect(Collectors.toList());
    }

    protected List<StepExecutionDto> mapStepExecutions(Collection<StepExecution> stepExecutions, Map<Long, StepRunReport> stepRunReportMap) {
        return stepExecutions.stream()
                .map(stepExecution -> mapStepExecution(stepExecution, stepRunReportMap.get(stepExecution.getId())))
                .collect(Collectors.toList());
    }

    protected ExecutionStatus mapBatchStatus(BatchStatus status) {
        return ExecutionStatus.valueOf(status.toString());
    }

    protected Map<String, Object> mapParameters(JobParameters jobParameters) {
        return jobParameters.getParameters().entrySet()
                .stream()
                .map(stringJobParameterEntry -> Map.entry(stringJobParameterEntry.getKey(), stringJobParameterEntry.getValue().getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected List<StepErrorDto> mapStepRunReport(StepRunReport stepRunReport) {
        if (stepRunReport == null) {
            return new ArrayList<>();
        }

        return stepRunReport.getErrors()
                .stream()
                .sorted(Comparator.comparing(StepError::getOrder))
                .map(this::toErrorDto)
                .collect(Collectors.toList());
    }

    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
}
