package cz.inqool.dl4dh.krameriusplus.core.system.export;

import cz.inqool.dl4dh.krameriusplus.core.domain.dao.sql.service.DatedService;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportDto;
import cz.inqool.dl4dh.krameriusplus.core.system.export.dto.BulkExportMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: store.find bude vracat dto takze service bude zbytocny
@Service
public class BulkExportService implements DatedService<BulkExport, BulkExportCreateDto, BulkExportDto> {

    @Getter
    private final BulkExportMapper mapper;

    @Getter
    private final BulkExportStore store;

    @Autowired
    public BulkExportService(BulkExportMapper mapper, BulkExportStore store) {
        this.mapper = mapper;
        this.store = store;
    }

    public BulkExportDto findByJobEvent(String jobEventId) {
        return mapper.toDto(store.findByJobEventId(jobEventId));
    }
}
