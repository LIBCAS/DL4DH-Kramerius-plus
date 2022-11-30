package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.ExecutionStatus;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.JobExecutionDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepErrorDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepExecutionDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.step.StepRunReportDto;
import cz.inqool.dl4dh.krameriusplus.api.exception.MissingObjectException;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepError;
import cz.inqool.dl4dh.krameriusplus.corev2.job.report.StepRunReport;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.KRAMERIUS_JOB_INSTANCE_ID;
import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
public class KrameriusJobInstanceMapper {

    private JobExplorer jobExplorer;

    public KrameriusJobInstanceDto toDto(KrameriusJobInstance entity) {
        if (entity == null) {
            return null;
        }

        KrameriusJobInstanceDto krameriusJobInstanceDto = new KrameriusJobInstanceDto();

        krameriusJobInstanceDto.setId(entity.getId());
        krameriusJobInstanceDto.setJobStatus(entity.getExecutionStatus());
        krameriusJobInstanceDto.setJobType(entity.getJobType());
        krameriusJobInstanceDto.setExecutions(mapJobExecutions(entity));

        return krameriusJobInstanceDto;
    }

    public JobExecution toLastExecution(Long jobInstanceId) {
        JobInstance jobInstance = jobExplorer.getJobInstance(jobInstanceId);
        notNull(jobInstance, () -> new MissingObjectException(JobInstance.class, String.valueOf(jobInstanceId)));

        JobExecution lastExecution = jobExplorer.getLastJobExecution(jobInstance);
        notNull(lastExecution, () -> new MissingObjectException(JobExecution.class, String.valueOf(jobInstanceId)));
        return lastExecution;
    }

    private List<JobExecutionDto> mapJobExecutions(KrameriusJobInstance entity) {
        JobInstance jobInstance = jobExplorer.getJobInstance(entity.getJobInstanceId());
        notNull(jobInstance, () -> new MissingObjectException(JobInstance.class, String.valueOf(entity.getJobInstanceId())));
        List<JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);

        return jobExecutions.stream().map(jobExecution -> toJobExecutionDto(jobExecution, entity.getReports())).collect(Collectors.toList());
    }

    private JobExecutionDto toJobExecutionDto(JobExecution jobExecution, Map<Long, StepRunReport> stepRunReportMap) {
        if (jobExecution == null) {
            return null;
        }
        JobExecutionDto jobExecutionDto = new JobExecutionDto();

        jobExecutionDto.setStepExecutions(jobExecution.getStepExecutions().stream().map(
                stepExecution -> toStepExecutionDto(stepExecution, stepRunReportMap.get(stepExecution.getId()))
        ).collect(Collectors.toList()));

        jobExecutionDto.setStatus(ExecutionStatus.valueOf(jobExecution.getStatus().toString()));
        jobExecutionDto.setStartTime(jobExecution.getStartTime());
        jobExecutionDto.setCreateTime(jobExecution.getCreateTime());
        jobExecutionDto.setEndTime(jobExecution.getEndTime());
        jobExecutionDto.setLastUpdated(jobExecution.getLastUpdated());
        jobExecutionDto.setExitStatus(jobExecution.getExitStatus().toString());
        jobExecutionDto.setJobConfigurationName(jobExecution.getJobConfigurationName());
        jobExecutionDto.setJobParameters(jobExecution
                .getJobParameters().getParameters().entrySet().stream()
                .map(stringJobParameterEntry -> Map.entry(stringJobParameterEntry.getKey(), stringJobParameterEntry.getValue().getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));


        return jobExecutionDto;
    }

    public List<KrameriusJobInstanceDto> toDtoList(Map<Long, KrameriusJobInstance> entityMap) {
        return new TreeMap<>(entityMap).values()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public JobParameters toJobParameters(KrameriusJobInstance entity, Map<String, Object> nonIdentifyingParams) {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addString(KRAMERIUS_JOB_INSTANCE_ID, entity.getId());

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

    /**
     * This method should never be used, because we should not need to map DTO to ENTITY for EnrichmentRequest.
     * However, it needs to be declared if we want to use the generic DomainService interface so mapstruct can
     * generate it's mapping implementation for EnrichmentRequest. Otherwise, the project would not compile.
     */
    public Map<Long, KrameriusJobInstance> fromDtoList(List<KrameriusJobInstanceDto> dtos) {
        throw new UnsupportedOperationException("Mapping EnrichmentChainDto to EnrichmentChain is not supported.");
    }

    private StepExecutionDto toStepExecutionDto(StepExecution stepExecution, StepRunReport stepRunReport) {
        if (stepExecution == null) {
            return null;
        }

        StepExecutionDto stepExecutionDto = new StepExecutionDto();

        stepExecutionDto.setStepName(stepExecution.getStepName());
        stepExecutionDto.setExitStatus(stepExecution.getExitStatus().toString());
        stepExecutionDto.setReadCount(stepExecution.getReadCount());
        stepExecutionDto.setWriteCount(stepExecution.getWriteCount());
        stepExecutionDto.setCommitCount(stepExecution.getCommitCount());
        stepExecutionDto.setRollbackCount(stepExecution.getRollbackCount());
        stepExecutionDto.setReadSkipCount(stepExecution.getReadSkipCount());
        stepExecutionDto.setProcessSkipCount(stepExecution.getProcessSkipCount());
        stepExecutionDto.setWriteSkipCount(stepExecution.getWriteSkipCount());
        stepExecutionDto.setEndTime(stepExecution.getEndTime());
        stepExecutionDto.setStartTime(stepExecution.getStartTime());
        stepExecutionDto.setLastUpdated(stepExecution.getLastUpdated());
        stepExecutionDto.setTerminateOnly(stepExecution.isTerminateOnly());
        stepExecutionDto.setFilterCount(stepExecution.getFilterCount());
        stepExecutionDto.setReport(toStepRunReportDto(stepRunReport));

        return stepExecutionDto;
    }

    private StepRunReportDto toStepRunReportDto(StepRunReport stepRunReport) {
        if (stepRunReport == null) {
            return null;
        }

        StepRunReportDto stepRunReportDto = new StepRunReportDto();

        stepRunReportDto.setErrors(stepRunReport.getError().stream().map(this::toErrorDto).collect(Collectors.toSet()));

        return stepRunReportDto;
    }

    private StepErrorDto toErrorDto(StepError stepError) {
        if (stepError == null) {
            return null;
        }

        StepErrorDto stepErrorDto = new StepErrorDto();

        stepErrorDto.setShortMessage(stepError.getShortMessage());
        stepErrorDto.setStackTrace(stepError.getStackTrace());

        return stepErrorDto;
    }

    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
        this.jobExplorer = jobExplorer;
    }
}
