package cz.inqool.dl4dh.krameriusplus.service.system.exportrequest;

import com.querydsl.core.QueryResults;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Params;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.mongo.params.Sorting;
import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.system.bulkexport.BulkExportService;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.exportrequest.dto.ExportRequestMapper;
import cz.inqool.dl4dh.krameriusplus.service.system.jobplan.JobPlanService;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExportRequestService implements DatedService<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    @Getter
    private ExportRequestStore store;

    @Getter
    private ExportRequestMapper mapper;

    private JobPlanService jobPlanService;

    private BulkExportService bulkExportService;

    @Override
    @Transactional
    public ExportRequestDto create(@NonNull ExportRequestCreateDto createDto) {
        validateParams(createDto.getConfig().getParams());

        ExportRequest entity = mapper.fromCreateDto(createDto);

        jobPlanService.create(entity.getJobPlan());
        bulkExportService.create(entity.getBulkExport());

        return mapper.toDto(store.create(entity));
    }

    public void startExecution(ExportRequestDto exportRequestDto) {
        jobPlanService.startExecution(exportRequestDto.getJobPlan());
    }

    public QueryResults<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        QueryResults<ExportRequest> results = store.list(name, owner, isFinished, page, pageSize);

        return new QueryResults<>(mapper.toDtoList(results.getResults()), results.getLimit(), results.getOffset(), results.getTotal());
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

    @Autowired
    public void setStore(ExportRequestStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(ExportRequestMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJobPlanService(JobPlanService jobPlanService) {
        this.jobPlanService = jobPlanService;
    }

    @Autowired
    public void setBulkExportService(BulkExportService bulkExportService) {
        this.bulkExportService = bulkExportService;
    }
}
