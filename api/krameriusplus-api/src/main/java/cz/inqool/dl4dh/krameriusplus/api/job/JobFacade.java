package cz.inqool.dl4dh.krameriusplus.api.job;

import cz.inqool.dl4dh.krameriusplus.api.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.EnrichmentJobDto;
import cz.inqool.dl4dh.krameriusplus.api.batch.job.ExportJobDto;

public interface JobFacade {

    QueryResults<EnrichmentJobDto> listEnrichmentJobs(JobEventFilter filter, int page, int pageSize);

    QueryResults<ExportJobDto> listExportJobs(JobEventFilter filter, int page, int pageSize);

    EnrichmentJobDto findEnrichmentJob(String id);

    ExportJobDto findExportJob(String id);

    void restartJob(String id);

    void stopJob(String id);
}
