package cz.inqool.dl4dh.krameriusplus.corev2.job;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;
import cz.inqool.dl4dh.krameriusplus.api.job.JobEventFilter;
import cz.inqool.dl4dh.krameriusplus.api.job.JobFacade;
import org.springframework.stereotype.Component;

@Component
public class KrameriusJobInstanceFacade implements JobFacade {

    @Override
    public Result<KrameriusJobInstanceDto> listEnrichmentJobs(JobEventFilter filter, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public Result<KrameriusJobInstanceDto> listExportJobs(JobEventFilter filter, int page, int pageSize) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public KrameriusJobInstanceDto findJob(String id) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public void restartJob(String id) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }

    @Override
    public void stopJob(String id) {
        throw new UnsupportedOperationException("Not Yet Implemented.");
    }
}
