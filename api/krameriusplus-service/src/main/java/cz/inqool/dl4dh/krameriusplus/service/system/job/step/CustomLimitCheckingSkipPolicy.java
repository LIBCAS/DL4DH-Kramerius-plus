package cz.inqool.dl4dh.krameriusplus.service.system.job.step;

import cz.inqool.dl4dh.krameriusplus.core.domain.exception.GeneralException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.JobParameterKey.PAGE_ERROR_TOLERANCE;

@Component
@StepScope
public class CustomLimitCheckingSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Autowired
    public CustomLimitCheckingSkipPolicy(@Value("#{jobParameters['" + PAGE_ERROR_TOLERANCE + "']}") Integer pageSkipTolerance) {
        super(pageSkipTolerance == null ? 0 : pageSkipTolerance, Map.of(GeneralException.class, Boolean.TRUE));
    }
}
