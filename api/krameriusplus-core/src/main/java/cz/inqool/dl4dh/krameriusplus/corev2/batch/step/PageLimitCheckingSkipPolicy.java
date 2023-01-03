package cz.inqool.dl4dh.krameriusplus.corev2.batch.step;

import cz.inqool.dl4dh.krameriusplus.api.exception.GeneralException;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.PAGE_ERROR_TOLERANCE;

@Component
@StepScope
public class PageLimitCheckingSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Autowired
    public PageLimitCheckingSkipPolicy(@Value("#{jobParameters['" + PAGE_ERROR_TOLERANCE + "']}") Integer pageSkipTolerance) {
        super(pageSkipTolerance == null ? 0 : pageSkipTolerance,
                Map.of(GeneralException.class, Boolean.TRUE));
    }
}
