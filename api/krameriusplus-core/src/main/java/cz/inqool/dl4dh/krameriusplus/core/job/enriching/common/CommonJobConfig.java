package cz.inqool.dl4dh.krameriusplus.core.job.enriching.common;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonJobConfig {

    protected final Map<String, Step> steps = new HashMap<>();

    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Autowired
    public void setSteps(List<Step> steps) {
        steps.forEach(step -> this.steps.put(step.getName(), step));
    }
}