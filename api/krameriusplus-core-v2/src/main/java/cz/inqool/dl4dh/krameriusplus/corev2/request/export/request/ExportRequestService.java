package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.batch.KrameriusJobType;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstance;
import cz.inqool.dl4dh.krameriusplus.corev2.job.KrameriusJobInstanceService;
import cz.inqool.dl4dh.krameriusplus.corev2.job.config.JobParametersMapWrapper;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cz.inqool.dl4dh.krameriusplus.corev2.job.JobParameterKey.EXPORT_REQUEST_ID;

@Service
public class ExportRequestService implements DatedService<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    @Getter
    private ExportRequestStore store;

    @Getter
    private ExportRequestMapper mapper;

    private KrameriusJobInstanceService krameriusJobInstanceService;

    @Override
    public ExportRequestDto create(@NonNull ExportRequestCreateDto dto) {
        ExportRequest exportRequest = mapper.fromCreateDto(dto);
        exportRequest.setCreateRequestJob(createRequestJob(exportRequest));

        return mapper.toDto(store.save(exportRequest));
    }

    private KrameriusJobInstance createRequestJob(ExportRequest exportRequest) {
        JobParametersMapWrapper jobParametersMapWrapper = new JobParametersMapWrapper();
        jobParametersMapWrapper.putString(EXPORT_REQUEST_ID, exportRequest.getId());

        return krameriusJobInstanceService.createJobInstance(KrameriusJobType.CREATE_EXPORT_REQUEST,
                jobParametersMapWrapper);
    }

    public Result<ExportRequestDto> list(String name, String owner, boolean isFinished, int page, int pageSize) {
        Result<ExportRequest> requests = store.findByNameOwnerAndStatus(name, owner, isFinished, page, pageSize);
        List<ExportRequestDto> dtos = requests.getItems().stream().map(mapper::toDto).collect(Collectors.toList());

        return new Result<>(page, pageSize, requests.getTotal(), dtos);
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
}
