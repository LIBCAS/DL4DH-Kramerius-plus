package cz.inqool.dl4dh.krameriusplus.service.system.job.config.common.step;

import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StepContainer {

    private final Map<String, Step> steps = new HashMap<>();

    @Autowired
    public StepContainer(List<StepFactory> stepConfigurations) {
        registerStepBeans(stepConfigurations);
    }

    public Step getStep(String stepName) {
        return steps.get(stepName);
    }

    private void registerStepBeans(List<StepFactory> stepConfigurations) {
        stepConfigurations.forEach(stepConfig -> {
            Step step = stepConfig.build();

            this.steps.put(step.getName(), step);
        });
    }
}
