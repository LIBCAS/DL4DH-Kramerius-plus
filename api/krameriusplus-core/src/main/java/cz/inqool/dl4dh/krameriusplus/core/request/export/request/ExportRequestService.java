package cz.inqool.dl4dh.krameriusplus.core.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.RequestState;
import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.exception.ExportException;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportState;
import cz.inqool.dl4dh.krameriusplus.core.domain.jpa.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.job.JobParameterKey;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.core.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.core.job.config.JobParametersMapWrapper;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.Export;
import cz.inqool.dl4dh.krameriusplus.core.request.export.export.ExportStore;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportRequestService implements DatedService<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    @Getter
    private ExportRequestStore store;

    private ExportStore exportStore;

    @Getter
    private ExportRequestMapper mapper;

    private KrameriusJobInstanceService krameriusJobInstanceService;

    @Override
    public ExportRequestDto create(@NonNull ExportRequestCreateDto dto) {
        ExportRequest exportRequest = mapper.fromCreateDto(dto);
        exportRequest.setCreateRequestJob(createRequestJob(exportRequest));
        exportRequest.setBulkExport(createBulkExport());

        return mapper.toDto(store.save(exportRequest));
    }

    private BulkExport createBulkExport() {
        BulkExport bulkExport = new BulkExport();
        bulkExport.setState(ExportState.CREATED);

        return bulkExport;
    }

    private KrameriusJobInstance createRequestJob(ExportRequest exportRequest) {
        JobParametersMapWrapper jobParametersMapWrapper = new JobParametersMapWrapper();
        jobParametersMapWrapper.putString(JobParameterKey.EXPORT_REQUEST_ID, exportRequest.getId());

        return krameriusJobInstanceService.createJobInstance(KrameriusJobType.CREATE_EXPORT_REQUEST,
                jobParametersMapWrapper);
    }

    @Transactional(readOnly = true)
    public Result<ExportRequestDto> list(String name, String owner, Boolean isFinished, int page, int pageSize) {
        Result<ExportRequestView> requests = store.findByNameOwnerAndStatus(name, owner, isFinished, page, pageSize);
        List<ExportRequestDto> dtos = requests.getItems().stream().map(mapper::toDto).collect(Collectors.toList());

        return new Result<>(page, pageSize, requests.getTotal(), dtos);
    }

    @Transactional
    public ExportRequestDto cancel(String id) {
        ExportRequest exportRequest = findEntity(id);

        validateCancelConditions(exportRequest);

        List<KrameriusJobInstance> jobsToCancel = new ArrayList<>();
        boolean exportsStarted = false;

        // if request isn't created cancel the creation job only
        log.info("Cancelling export request with id: " + id);
        if (!exportRequest.getCreateRequestJob().getExecutionStatus().isFinished()) {
            log.info("Cancelling create request job for export request id: " + id);
            jobsToCancel.add(exportRequest.getCreateRequestJob());
        } else {
            exportsStarted = true;
            if (exportRequest.getBulkExport().getMergeJob() == null) {
                throw new IllegalStateException("ExportRequest with id: " + id + " has no merge job");
            }
            log.info("Cancelling merge job and export jobs for request id: " + id);
            List<KrameriusJobInstance> exportJobs = getExportJobsFromRequest(exportRequest);

            jobsToCancel.add(exportRequest.getBulkExport().getMergeJob());
            jobsToCancel.addAll(exportJobs);
        }

        krameriusJobInstanceService.cancelMultipleJobs(jobsToCancel);
        exportRequest.setState(RequestState.CANCELLED);

        // Cancel exports after jobs to prevent race with ExportJobListener
        if (exportsStarted) {
            cancelPendingExports(exportRequest);
        }

        log.info("Request cancelled, owned Jobs stopping");
        return mapper.toDto(store.save(exportRequest));
    }

    /**
     * Method collects all exports in CREATED state then cancels them at once
     *
     * @param exportRequest that exports will be cancelled under
     */
    private void cancelPendingExports(ExportRequest exportRequest) {
        Set<String> pending = exportRequest.getItems().stream()
                .flatMap(exportRequestItem -> collectPendingExports(exportRequestItem.getRootExport()).stream())
                .collect(Collectors.toSet());

        exportStore.cancelExports(pending);
    }

    private Set<String> collectPendingExports(Export export) {
        Set<String> result = new HashSet<>();

        if (export.getState().equals(ExportState.CREATED)) {
            result.add(export.getId());
            export.getChildrenList().forEach(child -> {
                if (child.getState().equals(ExportState.CREATED)) {
                    result.add(child.getId());
                    result.addAll(collectPendingExports(child));
                }
            });
        }

        return result;
    }

    private void validateCancelConditions(ExportRequest exportRequest) {
        String id = exportRequest.getId();
        if (exportRequest.getState().isComplete()) {
            throw new ExportException("ExportRequest with id: " + id + "is in state: " + exportRequest.getState(),
                    ExportException.ErrorCode.REQUEST_FINISHED);
        }

        if (exportRequest.getCreateRequestJob() == null) {
            throw new IllegalStateException("ExportRequest with id: " + id + " has no create request job");
        }
    }

    private List<KrameriusJobInstance> getExportJobsFromRequest(ExportRequest exportRequest) {
        return exportRequest.getItems().stream()
                .flatMap(item -> getJobsInTree(item.getRootExport()).stream())
                .collect(Collectors.toList());
    }

    private List<KrameriusJobInstance> getJobsInTree(Export rootExport) {
        List<KrameriusJobInstance> result = new ArrayList<>();
        getJobFromTree(rootExport, result);
        return result;
    }

    private void getJobFromTree(Export rootExport, List<KrameriusJobInstance> accumulator) {
        accumulator.add(rootExport.getExportJob());
        rootExport.getChildrenList().forEach(child -> getJobFromTree(child, accumulator));
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
    public void setKrameriusJobInstanceService(KrameriusJobInstanceService krameriusJobInstanceService) {
        this.krameriusJobInstanceService = krameriusJobInstanceService;
    }

    @Autowired
    public void setExportStore(ExportStore exportStore) {
        this.exportStore = exportStore;
    }
}
