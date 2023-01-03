package cz.inqool.dl4dh.krameriusplus.core.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@StepScope
public class PageLimitCheckingSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Autowired
    public PageLimitCheckingSkipPolicy(@Value("#{jobParameters['" + JobParameterKey.PAGE_ERROR_TOLERANCE + "']}") Integer pageSkipTolerance) {
        super(pageSkipTolerance == null ? 0 : pageSkipTolerance,
                Map.of(GeneralException.class, Boolean.TRUE));
    }
}
