package cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.service.system.job.exportrequest.dto.ExportRequestMapper;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ExportRequestService implements DatedService<ExportRequest, ExportRequestCreateDto, ExportRequestDto> {

    @Getter
    private final ExportRequestStore store;

    @Getter
    private final ExportRequestMapper mapper;

    public ExportRequestService(ExportRequestStore store, ExportRequestMapper mapper) {
        this.store = store;
        this.mapper = mapper;
    }
}
