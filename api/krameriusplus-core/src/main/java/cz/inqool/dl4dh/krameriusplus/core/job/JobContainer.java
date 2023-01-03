package cz.inqool.dl4dh.krameriusplus.core.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.core.utils.Utils;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobContainer {

    private final Map<KrameriusJobType, Job> jobs = new HashMap<>();

    public Job getJob(KrameriusJobType jobType) {
        Job job = jobs.get(jobType);
        Utils.notNull(job, () -> new IllegalStateException("No job found for jobType: " + jobType));

        return job;
    }

    @Autowired(required = false) // TODO: removed required = false after at least one job is initialized
    public void setJobs(List<Job> jobs) {
        for (Job job : jobs) {
            KrameriusJobType jobType = KrameriusJobType.valueOf(job.getName());

            if (this.jobs.containsKey(jobType)) {
                throw new IllegalStateException("Multiple job definitions found with jobName: " + job.getName());
            }

            this.jobs.put(jobType, job);
        }
    }
}
