package cz.inqool.dl4dh.krameriusplus.api.facade;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.api.dto.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.filter.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.domain.exception.ValidationException;
import cz.inqool.dl4dh.krameriusplus.core.system.export.BulkExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.system.export.ExportService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.ExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileRef;
import cz.inqool.dl4dh.krameriusplus.core.system.file.FileService;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.JobEvent;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.KrameriusJob;
import cz.inqool.dl4dh.krameriusplus.core.system.jobevent.dto.JobEventCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.jobeventconfig.dto.export.MergeExportsJobConfigDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlan;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.JobPlanService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.ScheduledJobEvent;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.jobplan.dto.JobPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static cz.inqool.dl4dh.krameriusplus.core.utils.Utils.notNull;

@Component
public class ExportFacadeImpl implements ExportFacade {

    private final JobPlanService jobPlanService;

    private final ExportService exportService;

    private final BulkExportService bulkExportService;

    private final FileService fileService;

    private final JobPlanMapper jobPlanMapper;

    @Autowired
    public ExportFacadeImpl(JobPlanService jobPlanService, ExportService exportService, BulkExportService bulkExportService,
                            FileService fileService, JobPlanMapper jobPlanMapper) {
        this.jobPlanService = jobPlanService;
        this.exportService = exportService;
        this.bulkExportService = bulkExportService;
        this.fileService = fileService;
        this.jobPlanMapper = jobPlanMapper;
    }

    @Override
    public BulkExportDto export(ExportRequestDto requestDto) {
        return createJobPlan(requestDto);
    }

    @Override
    public QueryResults<Export> list(String publicationId, int page, int pageSize) {
        return exportService.list(publicationId, page, pageSize);
    }

    @Override
    public FileRef getFile(String fileRefId) {
        return fileService.find(fileRefId);
    }

    @Override
    public ExportDto findByJobEvent(String jobEventId) {
        notNull(jobEventId, () -> new ValidationException("Parameter jobEventId cannot be null", ValidationException.ErrorCode.INVALID_PARAMETERS));

        return exportService.findByJobEvent(jobEventId);
    }

    @Override
    public BulkExportDto findBulkExport(String jobEventId) {
        return bulkExportService.findByJobEvent(jobEventId);
    }

    private BulkExportDto createJobPlan(ExportRequestDto requestDto) {
        validateParams(requestDto.getConfig().getParams());

        // create same exporting job for each publicationId
        JobPlanCreateDto jobPlanCreateDto = new JobPlanCreateDto();
        jobPlanCreateDto.setName(requestDto.getName());
        requestDto.getPublications().
                forEach(publicationId -> {
                    JobEventCreateDto jobEventCreateDto = new JobEventCreateDto();
                    jobEventCreateDto.setPublicationId(publicationId);
                    jobEventCreateDto.setJobName(requestDto.getName());
                    jobEventCreateDto.setConfig(requestDto.getConfig());
                    jobPlanCreateDto.getJobs().add(jobEventCreateDto);
        });

        // add merge job
        JobEventCreateDto jobEventCreateDto = new JobEventCreateDto();
        jobEventCreateDto.setConfig(new MergeExportsJobConfigDto());
        jobPlanCreateDto.getJobs().add(jobEventCreateDto);

        JobPlan jobPlan = jobPlanService.create(jobPlanCreateDto);
        JobPlanDto jobPlanDto = jobPlanMapper.toDto(jobPlan);

        BulkExportCreateDto bulkExportCreateDto = new BulkExportCreateDto();
        bulkExportCreateDto.setJobEvent(findMergeJob(jobPlan));

        BulkExportDto bulkExportDto = bulkExportService.create(bulkExportCreateDto);

        jobPlanService.startExecution(jobPlanDto);

        return bulkExportDto;
    }

    private JobEvent findMergeJob(JobPlan jobPlan) {
        return jobPlan.getScheduledJobEvents().stream()
                .map(ScheduledJobEvent::getJobEvent)
                .filter(jobEvent -> jobEvent.getConfig().getKrameriusJob() == KrameriusJob.EXPORT_MERGE)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Merging job not found in plan"));
    }


    private void validateParams(Params params) {
        if (!params.getIncludeFields().isEmpty()) {
            // Include index field, because it is used for naming export files;
            params.includeFields("index");
        }

        if (params.getSorting().isEmpty()) {
            params.getSorting().add(new Sorting("index", Sort.Direction.ASC));
        }
    }
}
