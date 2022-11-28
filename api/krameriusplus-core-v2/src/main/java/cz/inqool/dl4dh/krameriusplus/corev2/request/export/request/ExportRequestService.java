package cz.inqool.dl4dh.krameriusplus.corev2.request.export.request;

import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestCreateDto;
import cz.inqool.dl4dh.krameriusplus.api.export.ExportRequestDto;
import cz.inqool.dl4dh.krameriusplus.corev2.domain.jpa.service.DatedService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
