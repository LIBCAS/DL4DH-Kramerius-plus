package cz.inqool.dl4dh.krameriusplus.api.job;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.KrameriusJobInstanceDto;

public interface JobFacade {

    Result<KrameriusJobInstanceDto> listEnrichmentJobs(JobEventFilter filter, int page, int pageSize);

    Result<KrameriusJobInstanceDto> listExportJobs(JobEventFilter filter, int page, int pageSize);

    KrameriusJobInstanceDto findJob(String id);

    void restartJob(String id);

    void stopJob(String id);
}
