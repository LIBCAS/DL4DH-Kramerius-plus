package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.Result;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.DatedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportRequestService implements DatedService<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    @Getter
    private ExportRequestStore store;

    @Getter
    private ExportRequestMapper mapper;

    @Autowired
    public void setStore(ExportRequestStore store) {
        this.store = store;
    }

    @Autowired
    public void setMapper(ExportRequestMapper mapper) {
        this.mapper = mapper;
    }

    public Result<ExportRequestDto> listByNameOwnerAndStatus(String name, String owner, Boolean isFinished, int page, int pageSize) {
        List<ExportRequestDto> exportRequestDtos = store.findByNameOwnerAndStatus(name, owner, isFinished, page, pageSize)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return Result.<ExportRequestDto>builder()
                .items(exportRequestDtos)
                .page(page)
                .pageSize(pageSize)
                .total(exportRequestDtos.size())
                .build();
    }
}
