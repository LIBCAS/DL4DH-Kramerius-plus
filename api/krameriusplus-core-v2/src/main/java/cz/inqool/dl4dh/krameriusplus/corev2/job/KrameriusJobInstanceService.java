package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class KrameriusJobInstanceService {

    private KrameriusJobInstanceStore store;

    private KrameriusJobInstanceMapper mapper;

    private JobRepository jobRepository;

    @Transactional
    public KrameriusJobInstanceDto createJobDto(KrameriusJobType jobType, Map<String, Object> jobParametersMap) {
        return mapper.toDto(createJob(jobType, jobParametersMap));
    }

    @Transactional
    public KrameriusJobInstance createJob(KrameriusJobType jobType, Map<String, Object> jobParametersMap) {
        // 1. Create JobInstance from JobType + JobParameters, using mapper::toJobParameters
        // 2. Create KrameriusJobInstance with default executionStatus CREATED
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Transactional
    public void updateStatus(KrameriusJobInstance instance) {
        // 1. get last JobExecution from mapper
        // 2. set status and update KrameriusJobInstance
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    public KrameriusJobInstance find(String jobInstanceId) {
        return store.find(jobInstanceId);
    }

    @Autowired
    public void setStore(KrameriusJobInstanceStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(KrameriusJobInstanceMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

}
