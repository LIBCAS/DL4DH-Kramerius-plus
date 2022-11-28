package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.corev2.utils.Utils.notNull;

@Component
public class JobContainer {

    private final Map<KrameriusJobType, Job> jobs = new HashMap<>();

    public Job getJob(KrameriusJobType jobType) {
        Job job = jobs.get(jobType);
        notNull(job, () -> new IllegalStateException("No job found for jobType: " + jobType));

        return job;
    }

    @Autowired
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
